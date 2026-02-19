# 变更日志

本文档记录SkinToStatueAndroid项目的所有重要变更。

格式基于[Keep a Changelog](https://keepachangelog.com/)。

## [Unreleased]

### 新增
- ✨ 100%还原Python版本的所有功能
- ✨ 支持HD皮肤（64x, 128x, 256x, 512x）
- ✨ 双层皮肤处理
- ✨ 独立盔甲层级系统
- ✨ 三种输出格式（.schem, .mcstructure, .litematic）
- ✨ 五种颜色匹配算法
- ✨ 图像滤镜（色相、饱和度、亮度、对比度、色调分离）
- ✨ 现代简洁UI（Material Design 3）
- ✨ 深色模式支持
- ✨ 响应式配置管理
- ✨ GitHub Actions CI/CD
- ✨ 全面的测试套件
- ✨ 代码质量检查（Lint, Detekt）

### 修复
- 🐛 修复皮革染色算法，使用assets/公式.txt中的复杂公式
- 🐛 修复BlockIndex优先级系统，与Python版本完全相同
- 🐛 修复缓存系统，包含wantTransparent参数
- 🐛 修复HSBDiff变量名冲突
- 🐛 修复excludeFallingBlocks逻辑
- 🐛 修复SchematicGenerator的DataVersion类型（Int）
- 🐛 修复SchematicGenerator的Width/Height/Length类型（Short）

### 变更
- ♻️ 重构Config类为扁平化结构
- ♻️ 更新NBT生成逻辑，使用jNBT库
- ♻️ 优化图像处理算法，使用Android Bitmap API
- ♻️ 重构盔甲系统，支持独立层级

### 文档
- 📝 添加完整的README文档
- 📝 添加快速开始指南
- 📝 添加测试文档
- 📝 添加GitHub Actions文档
- 📝 添加贡献指南
- 📝 添加项目结构文档

## [1.0.0] - 2024-01-XX

### 新增
- 🎉 首次发布
- ✨ 完整的皮肤转换功能
- ✨ 支持所有颜色匹配算法
- ✨ 支持所有输出格式
- ✨ 支持HD皮肤
- ✨ 支持盔甲系统
- ✨ 现代化UI设计
- ✨ 深色模式支持
- ✨ 配置持久化
- ✨ 完整的测试覆盖
- ✨ CI/CD集成

---

## 版本说明

### 版本号格式

遵循[语义化版本](https://semver.org/)：

```
MAJOR.MINOR.PATCH

MAJOR - 不兼容的API更改
MINOR - 向后兼容的功能新增
PATCH - 向后兼容的问题修复
```

### 变更类型

- `新增` - 新功能
- `变更` - 现有功能的更改
- `弃用` - 即将移除的功能
- `移除` - 已移除的功能
- `修复` - 问题修复
- `安全` - 安全漏洞修复

### 图标说明

- ✨ 新功能
- 🐛 问题修复
- ♻️ 重构或优化
- 📝 文档更新
- ⚡ 性能改进
- 🔒 安全更新
- 🎨 UI/UX改进
- 🧪 测试更新
- 🔧 构建工具更新
- 📦 依赖更新

## 提交记录

详细的提交记录请查看：
- [GitHub Commits](https://github.com/YOUR_USERNAME/SkinToStatueAndroid/commits/main)

## 发布历史

### 1.0.0 (2024-01-XX)

首个稳定版本，包含所有核心功能。

- 皮肤加载和处理
- HD皮肤支持
- 双层皮肤
- 盔甲系统
- 多种输出格式
- 颜色匹配算法
- 图像滤镜
- UI界面
- 配置管理
- 测试覆盖
- CI/CD

---

## 贡献者

感谢所有贡献者！

如果你想贡献，请查看[贡献指南](CONTRIBUTING.md)。