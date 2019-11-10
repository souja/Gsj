package com.wangxiaobao.gsj.http;

import com.wangxiaobao.gsj.enity.dish.Dish;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jack on 2017/10/28.
 */

public interface RecommendApi {


    String ACTION_ADDDISH2THIRDORDER = "dish/noErp/addDish2ThirdOrder.htm";
    String ACTION_UPDATEDISH2THIRDORDER = "dish/noErp/updateDish2ThirdOrder.htm";

    /**
     * update recommend dish
     *
     * @return
     */
    @FormUrlEncoded
    @POST(RecommendApi.ACTION_UPDATEDISH2THIRDORDER)
    Observable<Response> updateRecommendDish(@FieldMap Map<String, String> maps);



    /**
     * add recommend dish
     *
     * @return
     */
    @FormUrlEncoded
    @POST(RecommendApi.ACTION_ADDDISH2THIRDORDER)
    Observable<Response> addRecommendDish(@FieldMap Map<String, String> maps);




    /**
     * delete recommend dish
     *
     * @return
     */
    @POST(Constant.BASE_URL+ Constant.ACTION_DELETE_RECOMMEND+"{id}.htm")
    Observable<Response> deleteRecommendDish(@Path("id") String id);



    /**
     * delete recommend dish
     *
     * @return
     */
    @POST(Constant.ACTION_UPDATE_RECOMMEND_DISH_SORT+"{ids}.htm")
    Observable<Response> sortRecommendDishList(@Path("ids") String ids);








    /**
     * load recommend dish list
     *
     * @return
     */
    @GET("aic/wap/merchant/popdish")
    Observable<Response<List<Dish>>> loadRecommendDishList(@Query("vendorShopId") String vendorShopId);


}
