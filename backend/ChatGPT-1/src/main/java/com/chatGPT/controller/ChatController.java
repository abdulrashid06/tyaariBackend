package com.chatGPT.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatGPT.Services.ApiKeyService;
import com.chatGPT.model.ChatMessage;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;

@RestController
@RequestMapping("/chatbot")
@CrossOrigin("*")
public class ChatController {
	
	@Autowired
	private ApiKeyService apiservice;
	
	@Value("${openai.model}")
	private String model;
	
//	@Value("${openai.key}")
	private String key;
	
	//  /v1/chat/completions	gpt-4, gpt-4-0613, gpt-4-32k, gpt-4-32k-0613, gpt-3.5-turbo, gpt-3.5-turbo-0613, gpt-3.5-turbo-16k, gpt-3.5-turbo-16k-0613
	
	@PostMapping("/chat")
	public String callChatGPT(@RequestBody ChatMessage prompt) {
		
		OpenAiService openaiService = new OpenAiService(key);
		ChatCompletionRequest chatRequest = ChatCompletionRequest.builder().messages(prompt.getMessages()).model("gpt-3.5-turbo-0613").build();
		
		return openaiService.createChatCompletion(chatRequest).getChoices().get(0).getMessage().getContent();
	}
	
	@PostMapping("/addkey{secretkey}")
	public ResponseEntity<String> secretKey(@PathVariable String secretkey) {
		String key = apiservice.addApiKey(secretkey);
		return new ResponseEntity<String>(key, HttpStatus.OK);
	}
	
	@GetMapping("/getkey/{id}")
	public ResponseEntity<String> getSecretKey(@PathVariable Integer id) {
		String apikey = apiservice.getApiKey(id);
		key = apikey;
		return new ResponseEntity<String>(apikey, HttpStatus.OK);
	}

}
