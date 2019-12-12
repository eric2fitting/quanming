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

import com.jizhi.dao.AccountCardDao;
import com.jizhi.dao.MatchDao;
import com.jizhi.dao.OrderTimeDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.AccountCard;
import com.jizhi.pojo.Animal;
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
	
	private String content1="恭喜你成功卖出小动物，请尽快联系买家付款并完成交易";
	private String content2="恭喜你成功匹配到小动物，请尽快付款并联系卖家完成交易";
	private String title="恭喜你匹配成功";

	//一对一匹配，写入数据库
	public void doMatch(Order order, Property property) {
		//预约id
		Integer orderId = order.getId();
		Integer buyerId = order.getUserId();//买家id
		//资产id
		Integer propertyId = property.getId();
		Integer sellerId = property.getUserId();//买家id
		//根据动物id查找利润率
		Integer animalId = order.getAnimalId();
		Animal animal=animalService.queryById(animalId);
		Integer profit = animal.getProfit();
		//动物的进价
		Double price = property.getPrice();
		BigDecimal p=new BigDecimal(price.toString());
		//计算卖价，保留两位小数
		BigDecimal b1 =new BigDecimal(profit.toString());
		BigDecimal b2 =new BigDecimal(95D);
		BigDecimal b3 =new BigDecimal(100D);
		Double sellPrice=p.multiply(b1.add(b2)).divide(b3, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
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
			String buyerCid = userDao.queryById(buyerId).getCid();
			String sellerCid = userDao.queryById(sellerId).getCid();
			//给双方发推送
			appPushUtil.pushMsg(buyerCid, title, content2);
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
		//根据用户id从预约表中查找预约成功的预约id
		List<Order> orders=orderService.querySuccessOrder(buyerId);
		//用于封装领养信息
		ArrayList<BuyingDetail> list = new ArrayList<BuyingDetail>();
		if(orders==null) {
			list=null;
		}else {
			//根据预约表的id查找match表的信息
			List<Match> matches=new ArrayList<Match>();
			for(Order order:orders) {
				Match match=matchDao.queryByOrderId(order.getId());
				matches.add(match);
			}
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
				buyingDetail.setMatchId(match.getId());
				buyingDetail.setPrice(match.getPrice());
				buyingDetail.setIsPaid(match.getBuyerConfirm());
				buyingDetail.setAnimalType(animal.getAnimalType());
				buyingDetail.setSize(animal.getSize());
				buyingDetail.setCycleProfit(animal.getCycle()+"天/"+animal.getProfit()+"%");
				//计算可出售的日期、时间
				Calendar instance = Calendar.getInstance();
				instance.add(Calendar.DATE, animal.getCycle());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String sellDate = simpleDateFormat.format(instance.getTime());//可出售的日期
				String sellTime=sellDate+" "+property.getBuyTime();
				buyingDetail.setSellTime(sellTime);
				//计算最后的付款日期、时间
				String nowDate = simpleDateFormat.format(new Date());
				HashMap<String, Object> map = new HashMap<String,Object>();
				map.put("startTime", property.getBuyTime());
				map.put("animalId", animalId);
				String lastTime=orderTimeDao.queryLastTime(map);
				buyingDetail.setLastPayTime(nowDate+":"+lastTime);
				list.add(buyingDetail);
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
			List<Property> Properties=propertyService.queryByUserId(userId);
			//用于封装喂养的动物详情
			ArrayList<FeedingDetail> list = new ArrayList<FeedingDetail>();
			if(Properties==null) {
				list=null;
			}else {
				for(Property property:Properties) {
					Integer animalId = property.getAnimalId();
					//根据animalId查询该动物的信息
					Animal animal = animalService.queryById(animalId);
					FeedingDetail feedingDetail = new FeedingDetail();
					feedingDetail.setId(property.getId());
					feedingDetail.setBuyTime(property.getBuyDate());
					feedingDetail.setAnimalType(animal.getAnimalType());
					feedingDetail.setSize(animal.getSize());
					Integer proftis=animal.getProfit()+95;//利润率
					feedingDetail.setPrice(property.getPrice()+"×"+proftis+"%");
					feedingDetail.setCycleProfit(animal.getCycle()+"天/"+(animal.getProfit()-5)+"%");
					feedingDetail.setProfit("价值*"+animal.getProfit());
					//转让出栏时间
					String buyTime = property.getBuyDate();//买入时间
					Integer days=animal.getCycle();//喂养天数
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date buyDate = simpleDateFormat.parse(buyTime);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(buyDate);
					calendar.add(Calendar.DATE, days);
					String sellTime = simpleDateFormat.format(calendar.getTime());
					feedingDetail.setSellTime(sellTime);
					if(property.getIsSold()==0) {
						feedingDetail.setState("喂养收益中");
					}else if (property.getIsSold()==1) {
						feedingDetail.setState("正在转让中");
					}
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
	 * 根据预约id查询匹配信息
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
		payInfo.setPrice(match.getPrice());
		payInfo.setIsConfirm(match.getBuyerConfirm());
		payInfo.setName(seller.getUserName());
		payInfo.setTel(seller.getTel());
		payInfo.setAccountCardList(list);
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
			
			String secondPsw = buyerDoPay.getSecondPsw();
			String token = request.getHeader("token");
			String user_id = redisService.get(token);
			Integer userId = Integer.parseInt(user_id);
			User user = userDao.queryById(userId);
			String secondpsw2 = user.getSecondpsw();
			if(secondPsw.equals(secondpsw2)) {
				//更改match_tb表状态和付款凭证
				hashMap.put("id", buyerDoPay.getMatchId());
				hashMap.put("payPic", payPic);
				matchDao.updatePayPic(hashMap);
				return "成功";
			}else{
				return "密码错误";
			}
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

}
