# GitHub Actions 设置清单

在第一次推送到GitHub之前，请完成以下检查清单：

## ✅ 必须完成

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

## 📋 验证清单

- [ ] 工作流文件已提交
  - `.github/workflows/build.yml`
  - `.github/workflows/pr-check.yml`
  - `.github/workflows/scheduled.yml`

- [ ] 测试文件已提交
  - `app/src/test/java/com/skintostatue/android/PerfectRestorationTest.kt`
  - `app/src/test/java/com/skintostatue/android/ComprehensiveTest.kt`

- [ ] Lint配置已提交
  - `app/lint.xml`

- [ ] Detekt配置已提交
  - `detekt.yml`

- [ ] 文档已提交
  - `GITHUB_ACTIONS.md`
  - `GITHUB_ACTIONS_QUICKSTART.md`
  - `TESTING.md`
  - `BADGES.md`

## 🚀 首次推送

1. 提交所有文件
   ```bash
   git add .
   git commit -m "feat: add GitHub Actions CI/CD"
   ```

2. 推送到GitHub
   ```bash
   git push origin main
   ```

3. 查看Actions运行
   - 进入GitHub仓库
   - 点击"Actions"标签
   - 查看工作流运行状态

4. 下载和测试APK
   - 进入工作流运行页面
   - 下载`app-debug`产物
   - 安装到Android设备测试

## 🔍 故障排除

### Actions未触发

- 确认分支名称正确（main或develop）
- 确认Actions已启用
- 检查工作流文件语法

### 构建失败

- 查看Actions日志
- 在本地运行：`./gradlew clean build`
- 修复问题后重新推送

### 徽章显示unknown

- 确认用户名已替换
- 等待第一次构建完成
- 检查徽章URL是否正确

## 📚 相关文档

- [GitHub Actions详细文档](GITHUB_ACTIONS.md)
- [GitHub Actions快速开始](GITHUB_ACTIONS_QUICKSTART.md)
- [测试文档](TESTING.md)
- [状态徽章使用](BADGES.md)
- [徽章替换说明](.github/BADGES_PLACEHOLDER.md)

## ✅ 完成后

恭喜！你已成功配置GitHub Actions CI/CD系统。

现在每次推送代码或创建Pull Request时，都会自动运行：
- 构建检查
- 单元测试
- 代码质量检查
- 覆盖率报告

定期（每天凌晨2点）会运行：
- 完整测试套件
- 性能测试
- 兼容性测试

---

如有问题，请查看相关文档或提交Issue。