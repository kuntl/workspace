package com.kunt.crm.util;

import java.util.UUID;

public class UUIDUtil {
	
	public static String getUUID(){
		
		return UUID.randomUUID().toString().replaceAll("-","");
		//?useUnicode=true&characterEncoding=utf8
	}
	
}
