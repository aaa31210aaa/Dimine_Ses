package utils;

import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.util.Map;

public class MyOkhttpUtils {
    public void GetOkHttp() {
    }

    public static PostFormBuilder PostOkHttp(String url, Map<String, String> maps) {
        PostFormBuilder builder = new PostFormBuilder();
        builder.url(url);
        //遍历参数到请求体
        for (String key : maps.keySet()) {
            builder.addParams(key, maps.get(key));
        }
        return builder;
    }

    private void PostFileOkhttp() {

    }


}
