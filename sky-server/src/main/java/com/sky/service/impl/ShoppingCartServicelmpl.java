package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServicelmpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入到购物车的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //如果已经存在了，只需要将数量加1
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);//update shopping_cart set number = ? where id = ?
            shoppingCartMapper.updateNumberById(cart);
        }else {
            //如果不存在，需要插入一条购物车数据

            //判断本次添加到的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //本次添加到购物车的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            }else {
                //本次添加到购物车的是套餐
                Long setmealId = shoppingCartDTO.getSetmealId();

                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());


            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        // 1. 从 ThreadLocal 中获取当前登录用户的 id (拦截器解析 token 后存进去的)
        Long userId = BaseContext.getCurrentId();

        // 2. 构造查询条件
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();

        // 3. 调用 Mapper 查询数据库
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }


    /**
     * 清空购物车
     */
    public void cleanShoppingCart() {
        // 1. 从 ThreadLocal 中获取当前登录用户的 id (拦截器解析 token 后存进去的)
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }
}
