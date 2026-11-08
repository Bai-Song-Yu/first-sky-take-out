package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 模拟支付（按订单id）
     * @param id
     */
    void paySuccessById(Long id);

    /**
     * 查询订单详情（含订单明细）
     * @param id
     * @return
     */
    OrderVO getOrderDetail(Long id);

    /**
     * 用户端订单分页查询（历史订单）
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult pageQueryUser(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 用户取消订单
     * @param id
     */
    void userCancelById(Long id) throws Exception;

    /**
     * 再来一单：把该订单的明细重新加入购物车
     * @param id
     */
    void repetition(Long id);

    /**
     * 催单
     * @param id
     */
    void reminder(Long id);

}
