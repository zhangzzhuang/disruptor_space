package com.basi.disruptor_ms.request;


import com.basi.disruptor_ms.entity.RequestDto;
import com.basi.disruptor_ms.entity.ResponseDto;

public class RequestDtoEvent {

  private RequestDto requestDto;

  /**
   * 数据库操作Command收集器
   */
  private final CommandCollector commandCollector = new CommandCollector();

  /**
   * 响应结果
   */
  private ResponseDto responseDto;

  public CommandCollector getCommandCollector() {
    return commandCollector;
  }

  public void clearForGc() {
    this.requestDto = null;
    this.commandCollector.getCommandList().clear();
    this.responseDto = null;
  }

  public boolean hasErrorOrException() {
    return responseDto != null && (responseDto.getErrorMessage() != null);
  }

  public RequestDto getRequestDto() {
    return requestDto;
  }

  public void setRequestDto(RequestDto requestDto) {
    this.requestDto = requestDto;
  }

  public void setResponseDto(ResponseDto responseDto) {
    this.responseDto = responseDto;
  }
  public ResponseDto getResponseDto() {
    return responseDto;
  }
}
