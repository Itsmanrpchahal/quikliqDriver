package com.quikliq.quikliqdriver.constants


import java.util.ArrayList


object Constant {
    // val BASE_URL = "https://professionaler.com/weed/api/index.php/"     //production
    val BASE_URL = "https://hotspotcover.com/weed/api/"     //production

    val DEVICE_UNIQUE_ID = "device_unique_id"
    val UN_AUTHORIZE = 401
    val SUCCESS = 200
    val SERVER_ERROR = 500
    val EROOR = 400

    //machine section
    val SELECTED_STORE = "sel_store"
    val SELECTED_STORE_FULL_NAME = "sel_store_full_name"
    val SELECTED_STORE_NAME = "sel_store_name"
    val MACHINE_SIS = "machine_sis"
    val MACHINE_TYPE = "machine_type"
    val STORE_CODE = "store_code"
    val MACH_TITLE = "Machine_title"
    val TYPE = "type"
    val MACHINE_NUMBER_POS = "machine_no_pos"
    val MACHINE_NAME = "machine_name"
    val STORE_NAME = "store_name"
    val MACHINE_TITLE = "Machine_title"
    val MACHINE_ONLYNAME = "Machine_onlyname"
    val UPDATED_TIME = "updated_time"
    val MACHINE_NUMBER = "Machine_number"
    val MACHINES_ARRAY = "Machines_array"
    val Filter_Type = "filter_type"
    val IS_FAV = "is_fav"
    //gift section
    val GIFT_IMAGE = "g_img"
    val GIFT_TITLE = "g_title"
    val GIFT_DESCRIPTION = "g_description"
    val GIFT_PARLOURS = "g_parlours"
    val GIFT_ARRAY = "g_array"
    val LABEL = "label"

    //tenant section
    val TENANT_IMAGE = "t_img"
    val TENANT_TITLE = "t_title"
    val TENANT_DESCRIPTION = "t_description"
    val TENANT_HOURS = "t_hours"
    val TENANT_ARRAY = "t_array"

    val IS_LOGGED_IN = "is_logged_in"


    val FLOORMAP_SELECTED_STORE = "f_sel_store"
    val FLOORMAP_SELECTED_STORE_FULL_NAME = "f_sel_store_full_name"
    val FLOORMAP_SELECTED_STORE_NAME = "f_sel_store_name"


    var parlourArraylist: ArrayList<String>? = null

    var tenantApiCall: Boolean = false
    var pushApiCall: Boolean = false
    var giftApiCall: Boolean = false
    var floormapApiCall: Boolean = false
    var cateringApiCall: Boolean = false
    var accessApicall: Boolean = false
    var machineCategeoryApicall: Boolean = false
    val ACTION_CODE = "action_code"
    val FCM_TOKEN = "fcm_token"
    val HAS_BADGE = "has_badge"
    val SEARCH_FILTER_TYPE_NAME = "search_filter_type_name"


}
