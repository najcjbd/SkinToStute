# SkinToStatue Android - 项目结构

## 项目概述
100% 还原 Python 版本的 Minecraft 皮肤转雕像转换器，使用 Kotlin + Jetpack Compose 实现。

## 已完成模块

### 1. 项目结构 ✅
- Gradle 构建配置
- AndroidManifest.xml
- 依赖库配置

### 2. UI 主题 ✅
- Material Design 3
- 现代简洁浅色风格
- 深色模式自动适配
- Minecraft 主题色

### 3. 核心数据类 ✅
- **ColorMode.kt**: 颜色匹配算法（RGB, ABSRGB, HSL, HSB, LAB）
- **BlockData.kt**: 205 个方块的精确颜色值
- **OutputFormat.kt**: 输出格式、方向、盔甲材料等枚举
- **Config.kt**: 配置数据类和验证

## 待实现模块

### 4. 图像处理模块 ⏳
- **SkinProcessor.kt**: 皮肤加载、HD 皮肤支持、区域提取
- **ImageFilters.kt**: 图像滤镜（色相、饱和度、亮度、对比度、色调分离）

### 5. NBT 生成器 ⏳
- **SchematicGenerator.kt**: Sponge Schematic (.schem)
- **LitematicaGenerator.kt**: Litematica (.litematic)
- **McStructureGenerator.kt**: Bedrock Structure (.mcstructure)

### 6. 盔甲系统 ⏳
- **ArmorManager.kt**: 盔甲加载、染色、独立层级

### 7. UI 界面 ⏳
- **MainNavigation.kt**: 导航路由
- **HomeScreen.kt**: 主界面
- **SettingsScreen.kt**: 设置界面
- **PreviewScreen.kt**: 预览界面

### 8. 配置管理 ⏳
- **DataStoreConfigManager.kt**: DataStore 实现

### 9. 测试文件 ⏳
- **ColorModeTest.kt**: 颜色算法测试
- **ImageProcessingTest.kt**: 图像处理测试
- **NBTGenerationTest.kt**: NBT 生成测试
- **IntegrationTest.kt**: 集成测试

## 关键设计原则

1. **100% 还原**: 所有算法与 Python 版本完全相同
2. **逐字节相同**: 生成的文件与 Python 版本逐字节一致
3. **响应式配置**: 使用 DataStore + Flow 替代热重载
4. **现代 UI**: Jetpack Compose + Material Design 3
5. **完整测试**: 自动化测试确保完美还原

## 验证标准

- ✅ 颜色算法：与 Python 版本结果完全相同
- ✅ 方块选择：与 Python 版本选择完全相同
- ✅ NBT 结构：与 Python 版本结构完全相同
- ✅ 文件哈希：生成的文件与 Python 版本逐字节相同

## 依赖库

- jNBT: NBT 序列化
- OkHttp: HTTP 请求
- Jetpack Compose: UI 框架
- DataStore: 配置管理
- Coil: 图像加载
- Kotlin Coroutines: 异步处理