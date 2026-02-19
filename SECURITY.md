# 安全政策

## 支持的版本

我们为以下版本提供安全更新：

| 版本 | 支持状态 |
|------|---------|
| 1.0.x | ✅ 支持 |
| 0.x | ❌ 不支持 |

## 报告安全漏洞

### 如何报告

**不要**在公开Issue中报告安全漏洞！

请通过以下方式报告：

1. **邮件报告**：
   - 发送到：security@example.com
   - 主题：[SECURITY] 安全漏洞报告

2. **私有问题**：
   - 在GitHub上创建私有Issue
   - 添加`security`标签
   - 设置为仅维护者可见

### 报告内容

请提供以下信息：

- 漏洞描述
- 影响范围
- 复现步骤
- 可能的修复建议
- 联系方式（可选）

### 报告流程

1. 我们会在48小时内确认收到报告
2. 我们会在7天内评估漏洞
3. 我们会在14天内修复漏洞
4. 我们会在修复后协调发布时间
5. 我们会在修复后公开披露

### 安全更新

安全更新会优先处理：

- 紧急漏洞：48小时内修复
- 高危漏洞：7天内修复
- 中危漏洞：14天内修复
- 低危漏洞：下一个版本

## 安全最佳实践

### 依赖管理

- 定期更新依赖库
- 使用最新稳定版本
- 检查已知漏洞

### 代码审查

- 所有代码必须经过审查
- 关注安全相关的代码
- 使用静态分析工具

### 测试

- 包含安全测试用例
- 测试输入验证
- 测试权限检查

### 加密

- 使用标准加密库
- 不实现自定义加密
- 使用强加密算法

### 数据存储

- 敏感数据加密存储
- 使用Android Keystore
- 不在日志中记录敏感信息

## 已知安全注意事项

### 权限

应用请求以下权限：

- `READ_EXTERNAL_STORAGE` - 读取皮肤文件
- `WRITE_EXTERNAL_STORAGE` - 保存 schematic文件

### 网络

应用可能需要网络访问：

- 从URL加载皮肤（可选功能）
- 检查更新（可选功能）

### 数据存储

- 配置数据存储在本地
- 不收集用户数据
- 不向第三方发送数据

## 安全审计

我们定期进行安全审计：

- 依赖库安全扫描
- 代码安全审查
- 渗透测试（可选）

## 安全披露

### 修复前

不公开披露漏洞信息，直到：

- 漏洞已修复
- 修复已发布
- 用户有足够时间更新

### 修复后

修复后我们会：

- 发布安全公告
- 更新CHANGELOG
- 通知受影响的用户

## 安全资源

### 学习资源

- [OWASP Mobile Top 10](https://owasp.org/www-project-mobile-top-10/)
- [Android安全最佳实践](https://developer.android.com/topic/security/best-practices)
- [Kotlin安全编程](https://kotlinlang.org/docs/security.html)

### 工具

- [Android安全扫描工具](https://developer.android.com/studio/build/shrink-code)
- [依赖扫描工具](https://github.com/dependency-check/dependency-check-action)

## 联系方式

### 安全团队

- 邮箱：security@example.com
- GitHub：@security-team

### 紧急联系

如需紧急联系，请通过以下方式：

- 提交带`security`标签的Issue
- 发送邮件到security@example.com

## 致谢

感谢安全研究人员的负责任披露！

---

**记住**：安全是每个人的责任。让我们一起构建更安全的软件！