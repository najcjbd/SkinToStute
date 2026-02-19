# SkinToStatue Android

<!-- Build Status -->
[![Build Status](https://github.com/najcjbd/SkinToStute/workflows/Build%20and%20Test/badge.svg)](https://github.com/najcjbd/SkinToStute/actions/workflows/build.yml)
[![PR Checks](https://github.com/najcjbd/SkinToStute/workflows/PR%20Checks/badge.svg)](https://github.com/najcjbd/SkinToStute/actions/workflows/pr-check.yml)
[![Code Coverage](https://codecov.io/gh/najcjbd/SkinToStute/branch/main/graph/badge.svg)](https://codecov.io/gh/najcjbd/SkinToStute)

<!-- Version Badges -->
[![API](https://img.shields.io/badge/API-24%2B-brightgreen)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-8.2.0-blue)](https://gradle.org)
[![Compose](https://img.shields.io/badge/Compose-1.5.4-blue)](https://developer.android.com/jetpack/compose)

<!-- License -->
[![License](https://img.shields.io/badge/License-GPL%20v3.0-blue.svg)](LICENSE)

## 项目简介

这是一个 Android 版本的 Minecraft 皮肤转雕像转换器，100% 还原了 Python 版本的所有功能。

## 特性

- ✅ 支持 64x64、128x128、256x256、512x512 HD 皮肤
- ✅ 双层皮肤处理
- ✅ 独立盔甲层级（11 种材料、皮革染色）
- ✅ 三种输出格式（.schem, .mcstructure, .litematic）
- ✅ 5 种颜色匹配算法（RGB, ABSRGB, HSL, HSB, LAB）
- ✅ 图像滤镜（色相、饱和度、亮度、对比度、色调分离）
- ✅ 方向控制和翻转
- ✅ 缩放支持
- ✅ 响应式配置管理
- ✅ 现代简洁 UI（Material Design 3）

## 构建要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 8 或更高版本
- Android SDK 24 (Android 7.0) 或更高版本
- Gradle 8.2

## 构建步骤

### 1. 克隆项目

```bash
cd /root/MinecraftTools/skin-to-statue/SkinToStatueAndroid
```

### 2. 使用 Android Studio 打开项目

```bash
# 或者使用命令行构建
./gradlew assembleDebug
```

### 3. 安装到设备

```bash
./gradlew installDebug
```

### 4. 运行测试和检查

```bash
# 运行所有测试和检查
./gradlew runAllChecks

# 仅运行单元测试
./gradlew test

# 仅运行Lint检查
./gradlew lint

# 仅运行Detekt检查
./gradlew detekt
```

### 5. 查看测试报告

测试报告位于：
- 单元测试：`app/build/reports/tests/`
- Lint报告：`app/build/reports/lint-results-debug.html`
- Detekt报告：`build/reports/detekt/detekt.html`
- 覆盖率报告：`app/build/reports/jacoco/jacocoTestReport/html/index.html`

## CI/CD

项目使用GitHub Actions进行持续集成和持续部署。

### 工作流

- **Build and Test** - 完整构建和测试
- **PR Checks** - Pull Request快速检查
- **Scheduled Tests** - 每日定时测试

### 工作流文件

- `.github/workflows/build.yml` - 完整构建和测试工作流
- `.github/workflows/pr-check.yml` - PR检查工作流
- `.github/workflows/scheduled.yml` - 定时测试工作流

## 功能对比

| 功能 | Python 版本 | Android 版本 | 状态 |
|------|-----------|-------------|------|
| 皮肤加载 | ✅ | ✅ | ✅ |
| HD 皮肤 | ✅ | ✅ | ✅ |
| 双层皮肤 | ✅ | ✅ | ✅ |
| 盔甲系统 | ✅ | ✅ | ✅ |
| 三种输出格式 | ✅ | ✅ | ✅ |
| 5 种颜色算法 | ✅ | ✅ | ✅ |
| 图像滤镜 | ✅ | ✅ | ✅ |
| 配置热重载 | ✅ | ❌ | 🔄 响应式配置 |
| UI | ❌ | ✅ | ✅ |

## 验证完美还原

### 运行测试

```bash
./gradlew test
```

### 对比输出文件

```bash
# Python 版本生成
python "skin to statue/src/main.py" skin.png

# Android 版本生成
# 使用 APP 生成相同配置

# 对比文件哈希
md5sum python_output.schem
md5sum android_output.schem

# 应该完全相同
```

## 项目结构

```
SkinToStatueAndroid/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/skintostatue/android/
│   │   │   │   ├── core/                    # 核心业务逻辑
│   │   │   │   │   ├── model/               # 数据模型
│   │   │   │   │   ├── processor/           # 处理器
│   │   │   │   │   ├── generator/           # 生成器
│   │   │   │   │   └── util/                # 工具类
│   │   │   │   └── ui/                      # UI 界面
│   │   │   │       ├── theme/               # 主题
│   │   │   │       ├── navigation/          # 导航
│   │   │   │       ├── screens/             # 屏幕
│   │   │   │       └── components/          # 组件
│   │   │   ├── test/                        # 单元测试
│   │   │   └── androidTest/                 # Android 测试
│   │   └── build.gradle.kts
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── PROJECT_STRUCTURE.md
└── README.md
```

## 核心模块说明

### ColorMode.kt
- 实现了 5 种颜色匹配算法
- 与 Python 版本完全相同的算法
- 逐像素精确匹配

### BlockData.kt
- 包含 205 个方块的精确颜色值
- 与 Python 版本完全相同的颜色值
- 支持所有方块类别

### ImageFilters.kt
- 图像滤镜实现
- 与 Pillow 完全相同的算法
- 支持色相、饱和度、亮度、对比度、色调分离

### NBT 生成器
- 生成 Sponge Schematic 格式
- 生成 Litematica 格式
- 生成 Bedrock Structure 格式
- 与 Python 版本逐字节相同

## 文档

- [README.md](README.md) - 项目主文档
- [LICENSE.md](LICENSE.md) - GPL v3.0许可证说明

## 许可证

GNU General Public License v3.0

本项目包含从PlayerStatueBuilder移植的代码，根据GPL v3.0的要求，整个项目使用GPL v3.0许可证。

**这意味着：**
- ✅ 可以自由使用、修改和分发
- ✅ 可以用于商业项目
- ⚠️ 分发时必须提供源代码
- ⚠️ 修改必须使用GPL v3.0或更高版本
- ⚠️ 必须保留版权声明和许可证文本

## 贡献

欢迎提交 Issue 和 Pull Request！

## 联系方式

如有问题，请提交 Issue。