# 测试文档

本文档说明如何运行SkinToStatueAndroid的所有测试和代码质量检查。

## 运行所有检查

```bash
# 在项目根目录执行
./gradlew runAllChecks
```

这将运行：
- 单元测试
- Android集成测试
- Lint检查
- Detekt静态代码分析

## 单独运行检查

### 单元测试

```bash
# 运行所有单元测试
./gradlew test

# 运行特定测试类
./gradlew test --tests com.skintostatue.android.PerfectRestorationTest
./gradlew test --tests com.skintostatue.android.ComprehensiveTest

# 查看测试覆盖率报告
./gradlew testDebugUnitTest jacocoTestReport
# 报告位于: app/build/reports/jacoco/jacocoTestReport/html/index.html
```

### Android集成测试

```bash
# 运行Android集成测试
./gradlew connectedAndroidTest

# 运行特定Android测试类
./gradlew connectedAndroidTest --tests com.skintostatue.android.*Test
```

### Lint检查

```bash
# 运行Android Lint
./gradlew lint

# Lint报告位于:
# - HTML: app/build/reports/lint-results-debug.html
# - XML: app/build/reports/lint-results-debug.xml
# - Text: app/build/reports/lint-results-debug.txt
```

### Detekt静态代码分析

```bash
# 运行Detekt
./gradlew detekt

# Detekt报告位于:
# - HTML: build/reports/detekt/detekt.html
# - XML: build/reports/detekt/detekt.xml
# - Text: build/reports/detekt/detekt.txt
```

## 测试覆盖范围

### PerfectRestorationTest.kt

此测试套件验证Android实现与Python版本产生完全相同的结果：

1. **testColorMatchingAlgorithms** - 验证所有颜色匹配算法
2. **testImageFilters** - 验证图像滤镜
3. **testConfigurationDefaults** - 验证配置默认值
4. **testArmorDyeing** - 验证皮革染色公式
5. **testSkinScaling** - 验证HD皮肤缩放
6. **testNBTBlockEncoding** - 验证NBT编码
7. **testIntegration** - 端到端集成测试

### ComprehensiveTest.kt

此测试套件覆盖所有边界条件和特殊情况：

1. **testBlockIndexPriority** - 验证方块索引优先级系统
2. **testTransparencyHandling** - 验证透明度处理（alpha阈值32和200）
3. **testDirectionAndOffset** - 验证方向和偏移转换
4. **testLitematicaMetadata** - 验证Litematica元数据
5. **testMcStructureBlockMapping** - 验证Java到Bedrock方块映射
6. **testConfigurationValidation** - 验证配置验证范围
7. **testAllColorModes** - 验证所有5种颜色模式
8. **testBlockGenerationEdgeCases** - 验证边界情况
9. **testSkinFormatDetection** - 验证皮肤格式检测
10. **testArmorLayerIndependence** - 验证盔甲层独立性
11. **testExcludeFallingBlocks** - 验证掉落方块排除
12. **testScalingReplication** - 验证缩放复制逻辑
13. **testYAxisFlip** - 验证Y轴翻转
14. **testAllOutputFormats** - 验证所有3种输出格式
15. **testCacheSystem** - 验证缓存系统

## 代码质量标准

### Lint规则

- 所有Lint错误必须在发布前修复
- Lint警告应该尽快修复
- 配置文件：`app/lint.xml`

### Detekt规则

- 所有代码风格问题必须修复
- 复杂度限制：
  - 方法最大复杂度：15
  - 类最大复杂度：600
  - 方法最大长度：60行
  - 参数最大数量：6
- 配置文件：`detekt.yml`

### 测试覆盖率

- 核心算法覆盖率：100%
- 整体代码覆盖率：>80%
- 所有颜色模式：完全覆盖
- 所有输出格式：完全覆盖

## CI/CD集成

在CI/CD管道中运行所有检查：

```yaml
# GitHub Actions示例
name: Run All Checks
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run all checks
        run: ./gradlew runAllChecks
      - name: Upload test reports
        uses: actions/upload-artifact@v3
        with:
          name: test-reports
          path: |
            app/build/reports/tests/
            app/build/reports/lint-*
            build/reports/detekt/
```

## 常见问题

### 测试失败

如果测试失败：

1. 检查测试输出日志
2. 确认所有依赖已正确安装
3. 清理并重新构建：
   ```bash
   ./gradlew clean build
   ```

### Lint错误

如果Lint报告错误：

1. 查看HTML报告：`app/build/reports/lint-results-debug.html`
2. 根据错误类型修复问题
3. 某些错误可以在`lint.xml`中配置为忽略（如果确实不需要修复）

### Detekt警告

如果Detekt报告警告：

1. 查看HTML报告：`build/reports/detekt/detekt.html`
2. 根据警告类型重构代码
3. 复杂度过高时，考虑拆分方法或类

## 持续改进

定期审查：

1. 测试覆盖率报告
2. Lint和Detekt报告
3. 新功能的测试用例
4. 测试性能和运行时间

## 贡献指南

提交代码前：

1. 运行`./gradlew runAllChecks`
2. 确保所有测试通过
3. 修复所有Lint错误
4. 解决所有Detekt警告
5. 为新功能添加测试用例
6. 更新测试覆盖率