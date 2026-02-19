# GitHub Actions 工作流

SkinToStatueAndroid项目使用GitHub Actions进行持续集成和持续部署（CI/CD）。

## 工作流说明

### 1. Build and Test (`build.yml`)

**触发条件**：
- Push到`main`或`develop`分支
- Pull Request到`main`或`develop`分支

**执行任务**：
- ✅ 构建Debug APK
- ✅ 运行单元测试
- ✅ 运行Detekt静态代码分析
- ✅ 运行Android Lint检查
- ✅ 运行Android设备测试（需要模拟器）
- ✅ 生成测试覆盖率报告
- ✅ 构建Release APK（仅main分支）

**输出产物**：
- `test-results` - 单元测试结果
- `lint-results` - Lint检查结果
- `detekt-results` - Detekt分析结果
- `app-debug` - Debug APK
- `app-release` - Release APK（仅main分支）

### 2. PR Checks (`pr-check.yml`)

**触发条件**：
- Pull Request被打开、更新或重新打开

**执行任务**：
- ✅ 快速构建检查
- ✅ 单元测试
- ✅ 代码质量检查（Detekt + Lint）
- ✅ 代码覆盖率报告
- ✅ APK大小检查（限制10MB）
- ✅ 依赖安全检查

**自动评论**：
工作流会在PR中自动评论测试结果和代码覆盖率。

### 3. Scheduled Tests (`scheduled.yml`)

**触发条件**：
- 每天UTC时间凌晨2点运行
- 可手动触发

**执行任务**：
- ✅ 完整测试套件
- ✅ 性能测试
- ✅ 兼容性测试（多平台）
- ✅ 覆盖率阈值检查（最低80%）

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

## 使用方法

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

### 手动触发工作流

1. 进入GitHub仓库
2. 点击"Actions"标签
3. 选择工作流（如"Scheduled Tests"）
4. 点击"Run workflow"按钮

## 查看结果

### 在GitHub上查看

1. 进入GitHub仓库
2. 点击"Actions"标签
3. 选择对应的工作流运行
4. 查看日志和报告

### 下载产物

1. 进入工作流运行页面
2. 滚动到"Artifacts"部分
3. 点击下载所需的产物

## 本地运行检查

在推送代码前，建议在本地运行相同的检查：

```bash
# 运行所有检查
./gradlew runAllChecks

# 仅运行单元测试
./gradlew test

# 仅运行Lint
./gradlew lint

# 仅运行Detekt
./gradlew detekt

# 生成覆盖率报告
./gradlew jacocoTestReport
```

## 故障排除

### 构建失败

1. 查看Actions日志
2. 确认所有依赖已正确安装
3. 检查本地构建是否成功：
   ```bash
   ./gradlew clean build
   ```

### 测试失败

1. 查看测试报告
2. 在本地运行失败的测试：
   ```bash
   ./gradlew test --tests *TestName*
   ```

### Lint错误

1. 查看Lint报告
2. 修复报告中的问题
3. 忽略某些检查（如果必要）：
   编辑`app/lint.xml`文件

### APK大小超限

如果APK超过10MB限制：
- 检查是否有不必要的资源
- 使用ProGuard进行代码压缩
- 配置APK拆分

## 推送前的检查清单

### 必须完成

- [ ] 替换README.md中的徽章URL（YOUR_USERNAME）
  ```bash
  ./replace-badges.sh YOUR_GITHUB_USERNAME
  ```

- [ ] 在GitHub仓库中启用Actions
  1. 进入仓库Settings
  2. 点击"Actions"
  3. 点击"General"
  4. 选择"Allow all actions and reusable workflows"
  5. 保存

- [ ] （可选）配置Release构建的Secrets
  - KEYSTORE_FILE
  - KEYSTORE_PASSWORD
  - KEY_ALIAS
  - KEY_PASSWORD

### 验证清单

- [ ] 工作流文件已提交
- [ ] 测试文件已提交
- [ ] Lint配置已提交
- [ ] Detekt配置已提交
- [ ] 文档已提交
- [ ] 徽章URL已替换

## 状态徽章

可以在README中添加状态徽章：

```markdown
[![Build Status](https://github.com/username/SkinToStatueAndroid/workflows/Build%20and%20Test/badge.svg)]
[![PR Checks](https://github.com/username/SkinToStatueAndroid/workflows/PR%20Checks/badge.svg)]
[![Code Coverage](https://codecov.io/gh/username/SkinToStatueAndroid/branch/main/graph/badge.svg)]
```

## 最佳实践

1. **推送前本地测试**：在推送代码前运行`./gradlew runAllChecks`
2. **编写测试**：为新功能添加测试用例
3. **保持覆盖率**：确保代码覆盖率不低于80%
4. **修复Lint问题**：及时修复Lint和Detekt警告
5. **小步提交**：频繁提交小而专注的更改
6. **查看日志**：在PR中仔细检查工作流日志

## 支持和反馈

如果遇到GitHub Actions相关问题：
1. 查看工作流日志
2. 检查此文档
3. 在仓库中提交Issue

---

更多信息请参考：
- [GitHub Actions文档](https://docs.github.com/en/actions)
- [Gradle文档](https://docs.gradle.org/)
- [Android文档](https://developer.android.com/)