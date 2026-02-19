#!/bin/bash

# SkinToStatueAndroid 测试脚本
# 运行所有测试和代码质量检查

set -e

echo "========================================="
echo "SkinToStatueAndroid 测试套件"
echo "========================================="
echo ""

# 检查Gradle wrapper是否存在
if [ ! -f "./gradlew" ]; then
    echo "错误: 找不到gradlew文件"
    echo "请确保在项目根目录运行此脚本"
    exit 1
fi

# 给gradlew添加执行权限
chmod +x ./gradlew

echo "1. 清理构建..."
./gradlew clean

echo ""
echo "2. 运行单元测试..."
./gradlew test --stacktrace

echo ""
echo "3. 运行Android集成测试..."
./gradlew connectedAndroidTest --stacktrace

echo ""
echo "4. 运行Lint检查..."
./gradlew lint

echo ""
echo "5. 运行Detekt静态代码分析..."
./gradlew detekt

echo ""
echo "========================================="
echo "所有检查完成！"
echo "========================================="
echo ""
echo "报告位置："
echo "  - 测试报告: app/build/reports/tests/"
echo "  - Lint报告: app/build/reports/lint-results-debug.html"
echo "  - Detekt报告: build/reports/detekt/detekt.html"
echo ""
echo "如果所有检查通过，代码已准备好提交。"