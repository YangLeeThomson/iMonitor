package com.ronglian.repository;

import java.io.IOException;
import java.util.List;

import com.ronglian.model.TransInfo;

public interface TransInfoRepository{
	List<TransInfo> getTransInfoByUnionIds(List<String> unionIds) throws IOException;
}
