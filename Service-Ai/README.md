# Service-Ai - AI问答服务

这是一个基于SiliconFlow API的AI问答服务，提供了简单易用的AI对话功能。

## 功能特性

- 🤖 支持多种AI模型（目前支持Qwen3-32B）
- 🚀 基于OkHttp的高性能HTTP客户端
- 🔧 Spring Boot集成，支持依赖注入
- 📝 完整的错误处理和异常管理
- 🌐 RESTful API接口

## 快速开始

### 1. 配置API密钥

在 `src/main/resources/application.yml` 中添加SiliconFlow API配置：

```yaml
siliconflow:
  api:
    key: "your-api-key-here"  # 从 https://cloud.siliconflow.cn/ 获取
    url: "https://api.siliconflow.cn/v1/chat/completions"
```

或者在 `application.properties` 中：

```properties
siliconflow.api.key=your-api-key-here
siliconflow.api.url=https://api.siliconflow.cn/v1/chat/completions
```

### 2. 使用方式

#### 方式一：直接调用服务类

```java
@Autowired
private AnalysisAnswer analysisAnswer;

public void example() {
    try {
        String answer = analysisAnswer.askQuestion(
            SiliconflowModelEnum.QWEN3_32B, 
            "你好，请介绍一下自己"
        );
        System.out.println("AI回答: " + answer);
    } catch (IOException e) {
        System.err.println("网络异常: " + e.getMessage());
    } catch (RuntimeException e) {
        System.err.println("API异常: " + e.getMessage());
    }
}
```

#### 方式二：通过REST API

**基础问答接口：**

```bash
POST /ai/ask
Content-Type: application/json

{
  "prompt": "你好，请介绍一下自己"
}
```

**指定模型问答接口：**

```bash
POST /ai/ask-with-model
Content-Type: application/json

{
  "prompt": "你好，请介绍一下自己",
  "model": "QWEN3_32B"
}
```

**响应格式：**

```json
{
  "success": true,
  "answer": "你好！我是通义千问，一个由阿里云开发的AI助手...",
  "model": "Qwen/Qwen3-32B"
}
```

## 支持的模型

当前支持的AI模型（在 `SiliconflowModelEnum` 中定义）：

- `QWEN3_32B`: 通义千问 Qwen3-32B 模型

## 核心类说明

### AnalysisAnswer

主要的AI问答服务类，提供以下方法：

- `askQuestion(SiliconflowModelEnum model, String prompt)`: 发送AI问答请求
- `parseResponse(String responseBody)`: 解析API响应

### SiliconflowModelEnum

AI模型枚举类，定义了可用的模型：


### AiController

REST API控制器，提供HTTP接口：

- `POST /ai/ask`: 基础问答接口
- `POST /ai/ask-with-model`: 支持指定模型的问答接口

## 错误处理

服务包含完整的错误处理机制：

- **网络异常**: `IOException` - 网络连接问题
- **API异常**: `RuntimeException` - API调用失败或响应解析错误
- **参数异常**: `IllegalArgumentException` - 不支持的模型类型

## 依赖说明

项目使用了以下主要依赖：

- `okhttp3`: HTTP客户端
- `jackson`: JSON处理
- `spring-boot-starter-web`: Spring Web支持
- `lombok`: 简化代码

## 注意事项

1. **API密钥安全**: 请妥善保管您的SiliconFlow API密钥，不要提交到版本控制系统
2. **请求频率**: 注意API的调用频率限制，避免过于频繁的请求
3. **异常处理**: 在生产环境中请确保正确处理所有可能的异常
4. **模型选择**: 不同模型有不同的特点和费用，请根据需求选择合适的模型

## 扩展模型

要添加新的AI模型，请在 `SiliconflowModelEnum` 中添加新的枚举值：

```java
public enum SiliconflowModelEnum {
    QWEN3_32B("Qwen/Qwen3-32B"),
    // 添加新模型
    NEW_MODEL("Provider/ModelName");
    
    // ...
}
```

## 许可证

本项目遵循相应的开源许可证。