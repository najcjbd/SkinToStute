# 徽章配置说明

## 重要提醒

在将项目推送到GitHub之前，需要替换README.md中的徽章URL中的`YOUR_USERNAME`。

## 需要替换的位置

### README.md

找到以下行：

```markdown
[![Build Status](https://github.com/YOUR_USERNAME/SkinToStatueAndroid/workflows/Build%20and%20Test/badge.svg)](https://github.com/YOUR_USERNAME/SkinToStatueAndroid/actions/workflows/build.yml)
[![PR Checks](https://github.com/YOUR_USERNAME/SkinToStatueAndroid/workflows/PR%20Checks/badge.svg)](https://github.com/YOUR_USERNAME/SkinToStatueAndroid/actions/workflows/pr-check.yml)
[![Code Coverage](https://codecov.io/gh/YOUR_USERNAME/SkinToStatueAndroid/branch/main/graph/badge.svg)](https://codecov.io/gh/YOUR_USERNAME/SkinToStatueAndroid)
```

将`YOUR_USERNAME`替换为你的GitHub用户名。

## 示例

如果你的GitHub用户名是`johndoe`，替换后应该是：

```markdown
[![Build Status](https://github.com/johndoe/SkinToStatueAndroid/workflows/Build%20and%20Test/badge.svg)](https://github.com/johndoe/SkinToStatueAndroid/actions/workflows/build.yml)
[![PR Checks](https://github.com/johndoe/SkinToStatueAndroid/workflows/PR%20Checks/badge.svg)](https://github.com/johndoe/SkinToStatueAndroid/actions/workflows/pr-check.yml)
[![Code Coverage](https://codecov.io/gh/johndoe/SkinToStatueAndroid/branch/main/graph/badge.svg)](https://codecov.io/gh/johndoe/SkinToStatueAndroid)
```

## 自动化脚本

可以使用以下脚本自动替换：

```bash
#!/bin/bash
# replace-badges.sh

USERNAME="YOUR_GITHUB_USERNAME"

sed -i "s|YOUR_USERNAME|$USERNAME|g" README.md

echo "已替换徽章URL中的用户名为: $USERNAME"
```

使用方法：

```bash
chmod +x replace-badges.sh
./replace-badges.sh
```

## Codecov配置

如果项目还没有上传到Codecov，需要：

1. 在[Codecov](https://codecov.io/)注册账号
2. 添加GitHub仓库
3. 获取上传token（可选）
4. 确保GitHub Actions工作流可以上传覆盖率报告

## 验证徽章

替换后，推送代码到GitHub：

```bash
git add README.md
git commit -m "docs: update badges with correct username"
git push origin main
```

然后检查README.md，徽章应该显示正确的状态。

## 故障排除

### 徽章显示"unknown"

- 确认工作流文件已提交
- 确认分支名称正确
- 等待第一次构建完成

### Codecov徽章不显示

- 确认已在Codecov添加仓库
- 等待第一次覆盖率上传完成
- 检查Codecov token配置

---

记得：**在第一次推送到GitHub前完成此配置！**