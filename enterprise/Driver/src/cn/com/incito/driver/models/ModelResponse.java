
package cn.com.incito.driver.models;

/**
 * ModelResponse
 * 
 * @description ModelResponse
 * @author qiujiaheng
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class ModelResponse {

    /** 请求结果状态OK */
    public static final int REQUEST_RESULT_STATUS_OK = 0;

    /** 网络无效 */
    public static final int REQUEST_RESULT_STATUS_NETOWRK_NOT_AVALIABLE = -10001;

    /** 不用过期数据刷新界面 */
    public static final int REQUEST_RESULT_STATUS_NEED_REFRESH_WITHOUT_EXPRIED_DATA = -10002;

    /** 用过期数据刷新 */
    public static final int REQUEST_RESULT_STATUS_NEED_REFRESH_WITH_EXPRIED_DATA = -10003;

    /** 需要加载更多 */
    public static final int REQUEST_RESULT_STATUS_NEED_LOAD_MORE = -10004;

    /** 刷新完成了 */
    public static final int REQUEST_RESULT_STATUS_REFRESH_FINISH = -10005;

    /** 刷新完成了 */
    public static final int REQUEST_RESULT_STATUS_LOAD_MORE_FINISH = -10006;

    /** 没有更多数据 */
    public static final int REQUEST_RESULT_STATUS_NO_MORE_DATA = -10007;

    /** 没有数据 */
    public static final int REQUEST_RESULT_STATUS_NO_DATA = -10008;

    /** 数据库读取错误 */
    public static final int REQUEST_RESULT_STATUS_DATABSE_IO_ERROR = -10009;

    /** 重新加载数据 */
    public static final int REQUEST_RESULT_STATUS_RELOAD = -10010;

    private int status;

    private String msg;

    public ModelResponse() {
        super();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
