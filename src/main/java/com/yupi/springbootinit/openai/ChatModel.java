package com.yupi.springbootinit.openai;

import cn.hutool.json.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatModel {
    public String chart(String askString) throws IOException {
        // 请替换为您的API密钥
        String apiKey = "sk-dtX6zUjP8qDUA7g87aAbC0C428Da4c99B0Ed564cEe6cE7Ca";

        OkHttpClient client = new OkHttpClient();

        // 构建请求体
        MediaType mediaType = MediaType.parse("application/json");

        String systemString = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n分析需求：\n{数据分析的需求或者目标}\n原始数据：\n{csv格式的原始数据，用,作为分隔符}\n请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n【【【【【\n{前端 Echarts V5 的 option 配置对象js代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\n【【【【【\n{明确的数据分析结论、越详细越好，不要生成多余的注释}";
        String userString = "分析需求：\n分析网站用户的增长情况\n原始数据：\n日期,用户数\n1号,10\n2号,20\n3号,30";
        String assistantString = "【【【【【\n{\n\"title\": {\n\"text\": \"网站用户增长情况\",\nsubtext: \"\"\n},\n\"tooltip\": {\n\"trigger\": \"axis\",\n\"axisPointer\": {\n\"type\": \"shadow\"\n}\n},\n\"legend\": {\n\"data\": [\"用户数\"]\n},\n\"xAxis\": {\n\"data\": [\"1号\", \"2号\", \"3号\"]\n},\n\"yAxis\": {},\n\"series\": [{\n\"name\": \"用户数\",\n\"type\": \"bar\",\n\"data\": [10, 20, 30]\n}]\n}\n【【【【【\n根据数据分析可得，该网站用户数量逐日增长，时间越长，用户数量增长越多。";

        // 创建一个空的 JSON 对象
        JSONObject jsonObject = new JSONObject();
        // 向 JSON 对象中添加键值对
        jsonObject.put("model", "gpt-3.5-turbo");
        jsonObject.put("temperature", 0);
        //systemRoleJson
        JSONObject systemRoleJson = new JSONObject();
        systemRoleJson.put("role", "system");
        systemRoleJson.put("content", systemString);
        //userRoleJson
        JSONObject userRoleJson = new JSONObject();
        userRoleJson.put("role", "user");
        userRoleJson.put("content", userString);
        //assistantRoleJson
        JSONObject assistantRoleJson = new JSONObject();
        assistantRoleJson.put("role", "assistant");
        assistantRoleJson.put("content", assistantString);
        //askJson
        JSONObject askJson = new JSONObject();
        askJson.put("role", "user");
        askJson.put("content", askString);
        //msgList
        List msgList = new ArrayList<>();
        msgList.add(systemRoleJson);
        msgList.add(userRoleJson);
        msgList.add(assistantRoleJson);
        msgList.add(askJson);
        jsonObject.put("messages", msgList);

        String jsonBody = jsonObject.toString();

        RequestBody requestBody = RequestBody.create(mediaType, jsonBody);

        Request request = new Request.Builder()
                .url("https://api.zhiyungpt.com/v1/chat/completions")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            //取出content内容
            JSONObject responseJson = new JSONObject(responseBody);
            return responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getStr("content");
        } else {
            return response.message();
        }
    }
}
