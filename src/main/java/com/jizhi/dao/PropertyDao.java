package com.jizhi.dao;

import com.jizhi.pojo.Property;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PropertyDao {

	List<Property> queryCanSell(HashMap<String, Object> map);


	void updateState(Property property);

	Double queryTotalMonet(int userId);


	Property queryById(Integer id);


	List<Property> queryByUserId(Integer userId);


	List<Property> queryIsSelling(HashMap<String, Object> map);


	void add(Property property);
	
	//更改为已售
	void updateToSold(Integer id);


	List<Property> queryNotMatched(Integer userId);


	void updateBuyDateTime(HashMap<String, Object> map);

	@Delete("delete from property where id>200")
	Integer deleteAll();

	@Select("select count(*) from property where animalId=#{animalId} and buyDate=#{buyDate} and buyTime=#{buyTime} and isSold>0")
	int querySellSize(@Param("animalId") Integer animalId, @Param("buyDate") String buyDate, @Param("buyTime") String buyTime);

	@Update("update property set can_sell=1")
	int updateCanSell();

	@Update("update property set can_sell=1 where userId=#{userId}")
	void updateCansellByUserId(@Param("userId") Integer userId);

	@Update("update property set can_sell=0 where userId=#{userId} and animalId=#{animalId} and buyTime=#{buyTime}")
	void updateCansellAfterOrder(@Param("userId") Integer userId,@Param("animalId") Integer animalId,@Param("buyTime") String buyTime);

	@Select("select * from property where isSold=0")
	List<Property> queryAllNotSell();

	@Update("update property set buyDate=#{buyDate} where id=#{id}")
	int updateBuyDateById(@Param("buyDate") String newDate, @Param("id") Integer id);
	
	/**
	 * 查询所有本该今天出手却因用户没有预约而无法出手的资产
	 * @param animalId
	 * @param buyDay
	 */
	@Select("select * from property where animalId=#{animalId} and buyDate<=#{buyDate} and isSold=0 and can_sell=1")
	List<Property> queryNotOrderAndSellList(@Param("animalId") Integer animalId,@Param("buyDate") String buyDate);

	@Select("select * from property where animalId=#{animalId} and userId=#{userId} and buyTime=#{buyTime} and isSold=0")
	List<Property> queryByUserIdAndBuyTime(@Param("animalId") Integer animalId, @Param("userId") Integer userId, @Param("buyTime") String buyTime);

	

}
