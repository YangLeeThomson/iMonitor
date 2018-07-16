package com.ronglian.dubboApi;

import com.ronglian.dto.PutData;

import java.util.List;

/**
 * @description: write to es
 * @author: tianjin
 * @email: eternity_bliss@sina.cn
 * @create: 2018-07-16 上午11:24
 */
public interface IEsWriterService {
    void insert(PutData etlData);
    void bulkInsert(List<PutData> dataList);

    /**
     * if not exist then insert
     * @param etlData
     */
    void upsert(PutData etlData);

    /**
     * if not exist then insert
     * @param dataList
     */
    void bulkUpsert(List<PutData> dataList);

}
