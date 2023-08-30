package com.yupi.springbootinit.manager;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.openai.ChatModel;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class AiManager {
    @Resource
    private YuCongMingClient yuCongMingClient;

    @Resource
    private ChatModel chatModel;

    /**
     * Ai对话（鱼皮）
     *
     * @param message
     * @return
     */
    public String doChat(long modelId, String message) {
        //构造请求参数
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        //获取响应结果
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        System.out.println(response.getData());
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Ai响应错误");
        }
        return response.getData().getContent();
    }

    /**
     * Ai对话（官方）
     *
     * @param request
     * @param openAiApiKey
     * @return
     */
    public String doChat(String message) throws IOException {
//        String message="分析需求：\\n分析网站用户的增长情况\\n原始数据：\\n日期,用户数\\n1号,10\\n2号,20\\n3号,30\\n4号,40";
        String response=chatModel.chart(message);
        //获取响应结果
        return chatModel.chart(message);
    }

}
