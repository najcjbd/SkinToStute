# 快速开始指南

欢迎使用SkinToStatueAndroid！本指南将帮助你在5分钟内完成项目设置。

## 📋 前置要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 8 或更高版本
- Android SDK 24 (Android 7.0) 或更高版本
- Git

## 🚀 5分钟快速设置

### 1. 克隆项目

```bash
cd /root/MinecraftTools/skin-to-statue/SkinToStatueAndroid
```

### 2. 打开项目

**使用Android Studio**：
1. 打开Android Studio
2. 选择"File" → "Open"
3. 选择`SkinToStatueAndroid`文件夹
4. 等待Gradle同步完成

### 3. 运行应用

1. 连接Android设备或启动模拟器
2. 点击"Run"按钮（绿色三角形）
3. 应用将安装到设备上运行

### 4. 测试功能

1. 选择一张皮肤图片
2. 选择输出格式（.schem, .mcstructure, .litematic）
3. 点击"转换"按钮
4. 下载生成的文件

## 📝 第一次构建

### 命令行构建

```bash
# 清理构建
./gradlew clean

# 构建Debug版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug

# 运行测试
./gradlew test

# 运行所有检查
./gradlew runAllChecks
```

### 查看APK

构建完成后，APK位于：
```
app/build/outputs/apk/debug/app-debug.apk
```

## 🧪 运行测试

### 单元测试

```bash
# 运行所有单元测试
./gradlew test

# 运行特定测试类
./gradlew test --tests PerfectRestorationTest

# 查看测试报告
open app/build/reports/tests/testDebugUnitTest/index.html
```

### Android设备测试

```bash
# 运行Android测试
./gradlew connectedAndroidTest

# 查看测试报告
open app/build/reports/androidTests/connected/index.html
```

## 📊 代码质量检查

### Lint检查

```bash
# 运行Lint
./gradlew lint

# 查看Lint报告
open app/build/reports/lint-results-debug.html
```

### Detekt检查

```bash
# 运行Detekt
./gradlew detekt

# 查看Detekt报告
open build/reports/detekt/detekt.html
```

### 测试覆盖率

```bash
# 生成覆盖率报告
./gradlew jacocoTestReport

# 查看覆盖率报告
open app/build/reports/jacoco/jacocoTestReport/html/index.html
```

## 🎯 常用任务

### 清理项目

```bash
./gradlew clean
```

### 重新构建

```bash
./gradlew clean build
```

### 查看依赖树

```bash
./gradlew app:dependencies
```

### 分析APK大小

```bash
./gradlew assembleDebug
```

然后查看：
```
app/build/outputs/apk/debug/
```

## 🔧 配置说明

### 修改输出格式

编辑`Config.kt`文件：

```kotlin
val outputFormat = OutputFormat.SPONGE_SCHEMATIC  // 可选: MCSTRUCTURE, LITEMATICA
```

### 修改颜色模式

```kotlin
val colorMode = ColorMode.LAB  // 可选: RGB, ABSRGB, HSL, HSB
```

### 修改方块类别

```kotlin
val blockCategories = listOf(
    BlockCategory.WOOL,
    BlockCategory.CONCRETE,
    BlockCategory.TERRACOTTA
)
```

## 📱 Android Studio技巧

### 快捷键

- `Ctrl + F9` - 运行应用
- `Ctrl + Shift + F9` - 调试应用
- `Ctrl + B` - 跳转到定义
- `Ctrl + Alt + L` - 格式化代码
- `Ctrl + /` - 注释/取消注释

### 常用操作

- **查看日志**：点击底部的"Logcat"标签
- **查看设备**：点击底部的"Device Manager"
- **查看文件结构**：点击左侧"Project"标签
- **搜索代码**：`Ctrl + Shift + F`

## 🐛 故障排除

### Gradle同步失败

```bash
# 清理并重新同步
./gradlew clean
./gradlew build --refresh-dependencies
```

### 模拟器启动失败

1. 检查Android SDK是否正确安装
2. 检查AVD配置
3. 尝试使用不同版本的系统镜像

### 应用安装失败

```bash
# 卸载现有应用
adb uninstall com.skintostatue.android

# 重新安装
./gradlew installDebug
```

### 测试失败

```bash
# 清理测试缓存
./gradlew clean

# 重新运行测试
./gradlew test
```

## 📚 更多资源

### 文档

- [完整README](README.md)
- [项目结构](PROJECT_STRUCTURE.md)
- [测试文档](TESTING.md)
- [GitHub Actions](GITHUB_ACTIONS.md)

### 示例

- [示例皮肤](skin/)
- [示例配置](app/src/main/java/com/skintostatue/android/core/model/Config.kt)

### 外部资源

- [Android开发者文档](https://developer.android.com/)
- [Kotlin文档](https://kotlinlang.org/docs/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Gradle文档](https://docs.gradle.org/)

## 💡 下一步

1. ✅ 完成项目设置
2. ✅ 运行应用并测试
3. ✅ 阅读源代码了解实现
4. ✅ 尝试不同的皮肤和配置
5. ✅ 自定义和扩展功能

## 🤝 获取帮助

- 查看文档
- 检查测试报告
- 查看日志输出
- 提交Issue

---

祝你使用愉快！🎉