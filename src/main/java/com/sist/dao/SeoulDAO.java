package com.sist.dao;
import java.util.*;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.sist.vo.SeoulVO;

import oracle.net.ns.SessionAtts;

import java.io.*;
public class SeoulDAO {
	private static SqlSessionFactory ssf;
	static
	{
		try 
		{
			Reader reader=Resources.getResourceAsReader("Config.xml");
			ssf=new SqlSessionFactoryBuilder().build(reader);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	//기능
	/*
	<select id="seoulListData" resultType="SeoulVO" parameterType="hashmap">
		SELECT no,post,title
		FROM ${table}
		ORDER BY no ASC
		OFFSET #{start} ROWS FETCH NEXT 12 ROWS ONLY
	</select>
	<select id="seoulTotalPage" resultType="int" parameterType="hashmap">
		SELECT CEIL(COUNT(*)/12.0) FROM ${table}
	</select>
	 */
	public static List<SeoulVO> seoulListData(Map map)
	{
		SqlSession session=ssf.openSession(); //getConnection
		List<SeoulVO> list=session.selectList("seoulListData",map);
		session.close();
		return list;
	}
	public static int seoulTotalPage(Map map)
	{
		SqlSession session=ssf.openSession(); //getConnection
		int total=session.selectOne("seoulTotalPage",map);
		session.close();
		return total;
	}
	/*
	 * 상세보기
	<update id="hitIncrement" parameterType="hashmap">
		UPDATE ${table} SET
		hit=hit+1
		WHERE no=#{no}
	</update>
	<select id="seoulDetailData" resultType="SeoulVO" parameterType="hashmap">
		SELECT *
		FROM ${table}
		WHERE no=#{no}
	</select>
	 */
	// 목록/검색 리스트
	// 상세보기 > 원 
	// DML: update,delete,insert
	/*
	 * 	데이터 읽기:select
	 *  데이터 갱신:insert/update,delete > commit이 있어야함
	 * 
	 */
	public static SeoulVO seoulDetailData(Map map)
	{
		SqlSession session=ssf.openSession(true); //true을 주면 commit이됨
		String t=(String)map.get("table");
		if(!t.endsWith("hotel"))
		session.update("hitIncrement",map);
		// session.commit(); //밑에는 필요없음  //트랜잭션(일괄처리)
		SeoulVO vo=session.selectOne("seoulDetailData",map);
		session.close();
		return vo;
				
	}
}
