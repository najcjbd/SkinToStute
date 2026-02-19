# GitHub Actions 快速开始

## 第一次设置

### 1. 启用Actions

1. 进入GitHub仓库
2. 点击"Settings"标签
3. 点击左侧"Actions"
4. 点击"General"
5. 向下滚动到"Actions permissions"
6. 选择"Allow all actions and reusable workflows"
7. 点击"Save"

### 2. 配置Secrets（可选，用于Release构建）

1. 进入GitHub仓库
2. 点击"Settings"标签
3. 点击左侧"Secrets and variables" → "Actions"
4. 点击"New repository secret"
5. 添加以下Secrets：

| Secret名称 | 值 |
|-----------|---|
| `KEYSTORE_FILE` | Base64编码的keystore文件 |
| `KEYSTORE_PASSWORD` | Keystore密码 |
| `KEY_ALIAS` | 密钥别名 |
| `KEY_PASSWORD` | 密钥密码 |

### 3. 生成Base64编码的keystore

```bash
# 在本地运行
base64 -i release.keystore > keystore.base64.txt

# 复制keystore.base64.txt的内容到KEYSTORE_FILE secret
```

## 日常使用

### 提交代码

```bash
# 添加文件
git add .

# 提交
git commit -m "feat: add new feature"

# 推送到main分支（触发完整构建）
git push origin main

# 或推送到develop分支
git push origin develop
```

### 创建Pull Request

```bash
# 创建新分支
git checkout -b feature/my-feature

# 进行更改
# ...

# 提交
git add .
git commit -m "feat: add my feature"

# 推送
git push origin feature/my-feature
```

然后在GitHub上创建Pull Request，PR Checks会自动运行。

## 监控构建

### 查看构建状态

1. 进入GitHub仓库
2. 点击"Actions"标签
3. 查看最近的运行记录

### 查看构建日志

1. 点击任意工作流运行
2. 查看每个步骤的日志
3. 如果有失败，查看错误信息

### 下载产物

1. 进入成功的工作流运行
2. 滚动到页面底部的"Artifacts"部分
3. 点击下载所需的产物

## 常见场景

### 场景1：推送后构建失败

**解决步骤**：

1. 查看Actions日志，找到失败原因
2. 在本地运行相同的检查：
   ```bash
   ./gradlew runAllChecks
   ```
3. 修复问题
4. 重新提交：
   ```bash
   git add .
   git commit -m "fix: resolve build failure"
   git push origin main
   ```

### 场景2：PR检查失败

**解决步骤**：

1. 查看PR中的自动评论
2. 点击Actions链接查看详细日志
3. 修复失败的检查
4. 推送更新：
   ```bash
   git add .
   git commit -m "fix: resolve PR check failures"
   git push origin feature/my-feature
   ```

### 场景3：覆盖率下降

**解决步骤**：

1. 查看PR中的覆盖率评论
2. 确认覆盖率下降的原因
3. 为新代码添加测试：
   ```bash
   ./gradlew test --tests *NewFeatureTest*
   ```
4. 确保覆盖率满足要求（最低80%）

### 场景4：Lint错误

**解决步骤**：

1. 查看Lint报告
2. 修复Lint问题
3. 如果某些Lint警告确实不需要修复，可以忽略：
   编辑`app/lint.xml`，添加：
   ```xml
   <issue id="SomeIssueId" severity="ignore" />
   ```
4. 重新提交

## 自动化任务

### 每日定时测试

工作流每天凌晨2点（UTC时间）自动运行：
- 完整测试套件
- 性能测试
- 兼容性测试
- 覆盖率检查

### PR自动评论

当创建或更新PR时，工作流会自动评论：
- 测试结果
- 代码覆盖率
- APK大小
- 依赖安全检查

### 自动发布

当推送到main分支并打tag时：
- 构建Release APK
- 创建GitHub Release
- 上传APK到Release

## 最佳实践

### 1. 推送前本地检查

```bash
# 在推送前运行所有检查
./gradlew runAllChecks

# 确保所有检查通过后再推送
```

### 2. 编写测试

为新功能添加测试：

```kotlin
@Test
fun testNewFeature() {
    // 测试代码
}
```

### 3. 保持高覆盖率

- 目标覆盖率：80%+
- 核心代码：100%覆盖
- 定期检查覆盖率报告

### 4. 及时修复问题

- 不要让失败的构建积压
- 尽快修复Lint和Detekt警告
- 保持代码质量标准

### 5. 使用有意义的提交信息

```bash
# 好的提交信息
git commit -m "feat: add LAB color mode support"
git commit -m "fix: resolve memory leak in image processing"
git commit -m "docs: update README with installation guide"

# 不好的提交信息
git commit -m "update"
git commit -m "fix"
git commit -m "wip"
```

## 故障排除

### 问题：Actions未触发

**解决方案**：

1. 检查分支名称是否匹配工作流配置
2. 检查工作流文件语法是否正确
3. 查看Actions日志

### 问题：构建超时

**解决方案**：

1. 优化构建速度
2. 使用Gradle缓存
3. 分割大型测试套件

### 问题：产物下载失败

**解决方案**：

1. 检查网络连接
2. 重新运行工作流
3. 确认构建成功

## 下一步

- 查看[GITHUB_ACTIONS.md](GITHUB_ACTIONS.md)了解更多细节
- 查看[TESTING.md](TESTING.md)了解测试
- 查看[BADGES.md](BADGES.md)添加状态徽章

---

需要帮助？在仓库中提交Issue。