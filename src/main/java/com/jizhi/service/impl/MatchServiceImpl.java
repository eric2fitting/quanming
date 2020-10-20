package com.jizhi.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jizhi.dao.AccountCardDao;
import com.jizhi.dao.ExchangeRateDao;
import com.jizhi.dao.FeedDao;
import com.jizhi.dao.MatchDao;
import com.jizhi.dao.OrderTimeDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.AccountCard;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Feed;
import com.jizhi.pojo.Match;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.BuyerDoPay;
import com.jizhi.pojo.vo.BuyingDetail;
import com.jizhi.pojo.vo.FeedingDetail;
import com.jizhi.pojo.vo.PayInfo;
import com.jizhi.service.AnimalService;
import com.jizhi.service.MatchService;
import com.jizhi.service.OrderService;
import com.jizhi.service.PropertyService;
import com.jizhi.util.AppPushUtil;
import com.jizhi.util.Base64ToImgUtil;
import com.jizhi.util.RedisService;
import com.jizhi.util.SMS;
@Transactional(rollbackFor = Exception.class)
@Service
public class MatchServiceImpl implements MatchService{
	@Autowired
	private AnimalService animalService;
	@Autowired
	private MatchDao matchDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private OrderTimeDao orderTimeDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AccountCardDao accountCardDao;
	@Autowired
	private Base64ToImgUtil base64ToImgUtil;
	@Autowired
	private AppPushUtil appPushUtil;
	@Autowired
	private FeedDao feedDao;
	@Autowired
	private ExchangeRateDao exchangeRateDao;
	
	private String title="恭喜你匹配成功";
	private String content1="【在线商铺】尊敬的EGP用户您好，恭喜您已成功匹配线上旺铺，赶快登陆APP查看吧！";
	private String content3="买家已付款，请尽快核实并确认";
	private String title1="买家已付款";
	private String content="【在线商铺】尊敬的EGP用户您好，您线上的旺铺已成功转让，赶快登陆APP核对确认吧！";
	
	//一对一匹配，写入数据库
	public void doMatch(Order order, Property property) {
		//预约id
		Integer orderId = order.getId();
		Integer buyerId = order.getUserId();//买家id
		//资产id
		Integer propertyId = property.getId();
		Integer sellerId = property.getUserId();//卖家id
		//根据动物id查找利润率
		Integer animalId = order.getAnimalId();
		Animal animal=animalService.queryById(animalId);
		Integer profit = animal.getProfit();
		//动物的进价
		Double price = property.getPrice();
		BigDecimal p=new BigDecimal(price.toString());
		//计算卖价，保留两位小数
		BigDecimal b1 =new BigDecimal(profit.toString());
		BigDecimal b3 =new BigDecimal(100D);
		Double sellPrice=p.multiply(b1.add(b3)).divide(b3, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		//往配对表中添加数据
		Match match = new Match();
		match.setOrderId(orderId);
		match.setPropertyId(propertyId);
		match.setPrice(sellPrice);
		match.setBuyerConfirm(0);
		match.setSellerConfirm(0);
		Integer i = add(match);
		//匹配成功
		if(i>0) {
			//添加成功后更改订单表和资产表状态
			orderService.updateState(orderId);
			Property property2 = new Property();
			property2.setId(propertyId);
			property2.setIsSold(1);
			propertyService.updateState(property2);	
			// 查找双方的cid
			User buyer=userDao.queryById(buyerId);
			User seller=userDao.queryById(sellerId);
			String buyerCid = buyer.getCid();
			String sellerCid = seller.getCid();
			//将饲料给买家退回一部分
			Feed feed = new Feed();
			feed.setDate(new Date());
			feed.setUserId(buyerId);
			feed.setType(6);
			//计算退回数据
			BigDecimal b11 =new BigDecimal(sellPrice); 
			BigDecimal b12 =new BigDecimal(3);
			BigDecimal max_b = new BigDecimal(animal.getMaxPrice());
			Double feedNum=(max_b.subtract(b11)).multiply(b12).divide(b3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			feed.setNum(feedNum);
			feedDao.insert(feed);
			//给买家发短信
			try {
				SMS.informMsg(buyer.getTel(), content1);
				//SMS.informMsg(seller.getTel(), content1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//给双方发推送
			appPushUtil.pushMsg(buyerCid, title, content1);
			appPushUtil.pushMsg(sellerCid, title, content1);
		}
	}
	
	@Override
	public Integer add(Match match) {
		return matchDao.add(match);
	}
	
	/**
	 * 领养中：详情列表
	 */
	@Override
	public List<BuyingDetail> showAllBuying(String token) {
		String string = redisService.get(token);
		//用户id,现在的身份是买家
		Integer buyerId=Integer.parseInt(string);
		//根据用户id从匹配表里查询还没确认的消息	
		List<Order> orders=orderService.querySuccessOrder(buyerId);
		//用于封装领养信息
		ArrayList<BuyingDetail> list = new ArrayList<BuyingDetail>();
		if(orders.size()==0) {
			list=null;
		}else {
			//根据预约表的id查找match表的信息
			List<Match> matches=new ArrayList<Match>();
			for(Order order:orders) {
				Match match=matchDao.queryByOrderId(order.getId());
				if(match!=null) {
					matches.add(match);
				}
			}
			if(matches.size()>0) {
				for(Match match:matches) {
					//资产表的id
					Integer propertyId = match.getPropertyId();
					//得到资产信息也就是卖家该条信息
					Property property=propertyService.queryById(propertyId);
					//得到动物类型及所有信息
					Integer animalId = property.getAnimalId();
					Animal animal = animalService.queryById(animalId);
					//将所有需要展示额信息封装到buyingDetail中
					BuyingDetail buyingDetail = new BuyingDetail();
					buyingDetail.setMatchId(match.getId());//匹配表id
					buyingDetail.setNumber(property.getCode());//区块编码
					buyingDetail.setPrice(match.getPrice());//价值
					buyingDetail.setIsPaid(match.getBuyerConfirm());//是否
					buyingDetail.setAnimalType(animal.getAnimalType());//物种
					buyingDetail.setSize(animal.getSize());//大小型号
					BigDecimal buyPrice = new BigDecimal(match.getPrice());
					BigDecimal lilv = new BigDecimal(animal.getProfit());
					BigDecimal b1 = new BigDecimal(100D);
					double sellPrice = buyPrice.multiply(lilv.add(b1)).divide(b1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//卖价
					buyingDetail.setCycleProfit(animal.getCycle()+"天/"+animal.getProfit()+"%"+"  "+sellPrice);//智能收益
					//计算可出售的日期、时间
					Calendar instance = Calendar.getInstance();
					instance.add(Calendar.DATE, animal.getCycle());
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String sellDate = simpleDateFormat.format(instance.getTime());//可出售的日期
					
					//计算最后的付款日期、时间
					String nowDate = simpleDateFormat.format(new Date());
					HashMap<String, Object> map = new HashMap<String,Object>();
					map.put("startTime", property.getBuyTime());
					map.put("animalId", animalId);
					String lastTime=orderTimeDao.queryLastTime(map);
					buyingDetail.setLastPayTime(nowDate+"  "+lastTime);//付款截止时间
					buyingDetail.setBuyerDate(nowDate+"  "+property.getBuyTime()+"-"+lastTime);//领养时间
					
					String sellTime=sellDate+"  "+property.getBuyTime()+"-"+lastTime;//转让时间
					buyingDetail.setSellTime(sellTime);
					list.add(buyingDetail);
				}
			}
		}
		return list;
	}
	
	/**
	 * 喂养中详情列表
	 */
	@Override
	public List<FeedingDetail> showAllFeeding(String token) {
		try {
			//从redis中得到用户id
			String string = redisService.get(token);
			Integer userId=Integer.parseInt(string);
			//根据userId从资产表中查询
			List<Property> Properties=propertyService.queryNotMatched(userId);
			//用于封装喂养的动物详情
			ArrayList<FeedingDetail> list = new ArrayList<FeedingDetail>();
			if(Properties.size()==0) {
				list=null;
			}else {
				for(Property property:Properties) {
					Integer animalId = property.getAnimalId();
					//根据animalId查询该动物的信息
					Animal animal = animalService.queryById(animalId);
					//根据动物id和开始时间查询截止时间
					HashMap<String, Object> mapp = new HashMap<String,Object>();
					mapp.put("animalId", animalId);
					mapp.put("startTime", property.getBuyTime());
					String endTime = orderTimeDao.queryLastTime(mapp);
					
					FeedingDetail feedingDetail = new FeedingDetail();
					feedingDetail.setNumber(property.getCode());//区块编码
					feedingDetail.setBuyTime(property.getBuyDate()+"  "+property.getBuyTime()+"-"+endTime);//领养时间
					
					feedingDetail.setAnimalType(animal.getAnimalType());//种类
					feedingDetail.setSize(animal.getSize());//大小
					feedingDetail.setPrice(property.getPrice()+"");//价值
		
					//计算出栏收益
					BigDecimal b1 = new BigDecimal(property.getPrice());
					BigDecimal b2 = new BigDecimal(animal.getProfit());
					BigDecimal b3 = new BigDecimal(100);
					double sellPrice = b1.multiply(b2.add(b3)).divide(b3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					feedingDetail.setCycleProfit(animal.getCycle()+"天/"+(animal.getProfit())+"%"+"  "+sellPrice);//只智能合约收益
					//转让出栏时间
					String buyTime = property.getBuyDate();//买入时间
					Integer days=animal.getCycle();//喂养天数
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date buyDate = simpleDateFormat.parse(buyTime);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(buyDate);
					calendar.add(Calendar.DATE, days);
					String sellTime = simpleDateFormat.format(calendar.getTime());
					feedingDetail.setSellTime(sellTime+"  "+property.getBuyTime()+"-"+endTime);//出栏时间
					feedingDetail.setState("喂养收益中");//当前状态
					list.add(feedingDetail);
				}
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	
	/**
	 * 根据预约id查询尚未完成的匹配信息
	 */
	@Override
	public Match queryByOrderId(Integer id) {
		return matchDao.queryByOrderId(id);
	}
	
	/**
	 * 根据资产id查找匹配信息
	 */
	@Override
	public Match queryByPropertyId(HashMap<String, Object> map) {
		return matchDao.queryByPropertyId(map);
	}
	
	
	/**
	 * 买家付款页
	 */
	@Override
	public PayInfo buyerToPay(Integer matchId) {
		//匹配信息
		Match match = matchDao.queryById(matchId);
		//根据匹配表得到资产表id
		Integer propertyId = match.getPropertyId();
		//根据资产表查询卖家信息
		Property property = propertyService.queryById(propertyId);
		Integer sellerId = property.getUserId();
		User seller = userDao.queryById(sellerId);
		List<AccountCard> list = accountCardDao.queryAll(sellerId);
		PayInfo payInfo = new PayInfo();
		payInfo.setPrice(calculateUsdtPrice(match.getPrice()));
		//计算火币网的价格
		payInfo.setUsdtPrice(match.getPrice());
		payInfo.setIsConfirm(match.getBuyerConfirm());
		payInfo.setName(seller.getUserName());
		payInfo.setTel(seller.getTel());
		payInfo.setAccountCardList(list);
		payInfo.setPayPic(match.getPayPic());
		return payInfo;
	}
	/**
	 * 买家确认付款
	 */
	@Override
	public String doPay(BuyerDoPay buyerDoPay, HttpServletRequest request) {
		String payPic = base64ToImgUtil.base64(buyerDoPay.getPayPic());
		if(StringUtils.isEmpty(payPic)) {
			return "请上传付款截图";
		}else {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			
			//String secondPsw = buyerDoPay.getSecondPsw();
			//String token = request.getHeader("token");
			//String user_id = redisService.get(token);
			//Integer userId = Integer.parseInt(user_id);
			//User user = userDao.queryById(userId);
			//String secondpsw2 = user.getSecondpsw();
			//if(secondPsw.equals(secondpsw2)) {
				//更改match_tb表状态和付款凭证
				Integer matchId = buyerDoPay.getMatchId();
				hashMap.put("id", matchId);
				hashMap.put("payPic", payPic);
				matchDao.updatePayPic(hashMap);
				//查找卖家的CID并发推送
				Match match = matchDao.queryById(matchId);
				Integer propertyId = match.getPropertyId();
				Property property = propertyService.queryById(propertyId);
				Integer sellerId = property.getUserId();
				User seller = userDao.queryById(sellerId);
				
				try {
					//给卖家发送短信
					SMS.informMsg(seller.getTel(), content);
				} catch (Exception e) {
					e.printStackTrace();
				}
				appPushUtil.pushMsg(seller.getCid(), title1, content3);
				return "成功";
		}
	}
	@Override
	public List<Match> queryAllByBuyerConfirm() {
		return matchDao.queryAllByBuyerConfirm();
	}
	@Override
	public List<Match> queryAllBySellerConfirm() {
		return matchDao.queryAllBySellerConfirm();
	}
	@Override
	public void updateSellerConfirm(Integer id) {
		matchDao.updateSellerConfirm(id);
		
	}

	@Override
	public void deleteById(Integer id) {
		matchDao.deleteById(id);
		
	}
	
	/**
	 * 驳回，将买家确认改为0
	 */
	@Override
	public Integer cancelSell(Integer id) {
		return matchDao.cancelSell(id);
	}

	@Override
	public Double calculateUsdtPrice(Double price) {
		BigDecimal b1 = new BigDecimal(price);
		BigDecimal b2 = new BigDecimal(exchangeRateDao.getRate());
		Double result=b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return result;
		
	}
}
