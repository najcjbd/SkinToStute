# 贡献指南

感谢你对SkinToStatueAndroid项目的兴趣！我们欢迎所有形式的贡献。

## 🤝 如何贡献

### 报告Bug

1. 检查[Issues](../../issues)确认bug未被报告
2. 创建新的Issue，使用"Bug Report"模板
3. 提供详细信息：
   - Android版本和设备型号
   - 复现步骤
   - 期望行为
   - 实际行为
   - 日志输出（如有）

### 提出新功能

1. 检查[Issues](../../issues)确认功能未被请求
2. 创建新的Issue，使用"Feature Request"模板
3. 描述功能需求：
   - 功能描述
   - 使用场景
   - 预期效果

### 提交代码

1. Fork仓库
2. 创建特性分支：`git checkout -b feature/amazing-feature`
3. 进行更改
4. 运行测试：`./gradlew runAllChecks`
5. 提交更改：`git commit -m "Add amazing feature"`
6. 推送到分支：`git push origin feature/amazing-feature`
7. 创建Pull Request

## 📝 代码规范

### Kotlin风格

遵循[Google Kotlin风格指南](https://kotlinlang.org/docs/coding-conventions.html)：

```kotlin
// 好的命名
val userName: String

// 不好的命名
val u: String
```

### 文件组织

```
com.skintostatue.android.core/
├── model/          // 数据模型
├── processor/      // 处理器
├── generator/      // 生成器
└── util/           // 工具类
```

### 注释规范

```kotlin
/**
 * 描述类或函数的功能
 *
 * @param paramName 参数描述
 * @return 返回值描述
 */
fun myFunction(paramName: String): String {
    // 实现代码
}
```

## 🧪 测试要求

### 测试覆盖率

- 核心代码：100%覆盖
- 整体代码：80%以上

### 测试命名

```kotlin
@Test
fun testColorMatching_RGBMode() {
    // 测试代码
}

@Test
fun testImageFilters_Saturation() {
    // 测试代码
}
```

### 运行测试

提交前必须运行：

```bash
./gradlew runAllChecks
```

## 🔍 代码审查

### Pull Request清单

在提交PR前，确保：

- [ ] 代码通过所有检查（test, lint, detekt）
- [ ] 添加了相应的测试
- [ ] 更新了文档（如需要）
- [ ] 提交信息清晰
- [ ] 没有引入不必要的依赖
- [ ] 代码风格一致

### 提交信息规范

使用[约定式提交](https://www.conventionalcommits.org/)：

```
feat: 添加新功能
fix: 修复bug
docs: 更新文档
test: 添加测试
refactor: 重构代码
style: 代码风格调整
chore: 构建/工具链更新
perf: 性能优化
```

示例：

```bash
git commit -m "feat: add LAB color mode support"
git commit -m "fix: resolve memory leak in image processing"
git commit -m "docs: update README with installation guide"
```

## 🏷️ 版本控制

### 分支策略

- `main` - 主分支，稳定版本
- `develop` - 开发分支
- `feature/*` - 功能分支
- `bugfix/*` - 修复分支
- `hotfix/*` - 紧急修复分支

### 分支命名

```
feature/color-matching-enhancement
bugfix/memory-leak
hotfix/critical-security-fix
```

## 📦 发布流程

### 发布新版本

1. 更新版本号
2. 更新CHANGELOG
3. 创建Git标签
4. 推送到GitHub
5. 创建GitHub Release

```bash
# 更新版本号
vim app/build.gradle.kts

# 提交
git add .
git commit -m "chore: bump version to 1.0.0"

# 创建标签
git tag -a v1.0.0 -m "Release version 1.0.0"

# 推送
git push origin main
git push origin v1.0.0
```

## 🎨 UI/UX贡献

### 设计原则

- 遵循Material Design 3指南
- 保持简洁现代风格
- 支持深色模式
- 确保可访问性

### 提交设计资源

- 使用Figma或Sketch设计
- 导出为SVG或PNG格式
- 在Issue中附上设计稿

## 📖 文档贡献

### 更新文档

- 确保文档清晰易懂
- 添加示例代码
- 更新截图
- 检查拼写和语法

### 文档位置

- `README.md` - 项目概述
- `QUICKSTART.md` - 快速开始
- `TESTING.md` - 测试文档
- `GITHUB_ACTIONS.md` - CI/CD文档

## 🔐 安全贡献

### 报告安全漏洞

不要在公开Issue中报告安全漏洞！

1. 发送邮件到项目维护者
2. 详细描述漏洞
3. 等待确认和修复
4. 公开披露

## 🌍 国际化

### 添加新语言

1. 创建`values-xx/strings.xml`（xx为语言代码）
2. 翻译所有字符串资源
3. 测试语言切换

### 语言代码

- `zh` - 中文
- `en` - 英语
- `ja` - 日语
- `ko` - 韩语

## 📜 许可证

贡献的代码将使用项目的许可证（GPL v3.0）。

由于本项目包含PlayerStatueBuilder的移植代码，根据GPL v3.0的要求，所有贡献和修改都必须在GPL v3.0下许可。

## 🏆 贡献者

所有贡献者将被列在项目的贡献者列表中。

### 成为贡献者

- 提交至少一个有效的Pull Request
- 代码被合并到主分支
- 同意贡献者许可协议

## 📞 联系方式

- 提交Issue
- 发送Pull Request
- 在Discussions中讨论

## 🎉 致谢

感谢所有贡献者的支持！

---

有问题？查看[FAQ](../../wiki)或提交Issue。