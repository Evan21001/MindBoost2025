# Contributing to MindBoost 2025

Thank you for your interest in contributing to MindBoost 2025! This document provides guidelines and information for contributors.

## 🤝 How to Contribute

### **Reporting Bugs**
- Use the [GitHub Issues](https://github.com/Evan21001/MindBoost2025/issues) page
- Include detailed steps to reproduce the bug
- Provide device information and Android version
- Attach screenshots if applicable

### **Suggesting Features**
- Open a new issue with the "enhancement" label
- Describe the feature in detail
- Explain the use case and benefits
- Consider implementation complexity

### **Code Contributions**
1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

## 📋 Development Guidelines

### **Code Style**
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Keep functions small and focused

### **Architecture**
- Follow MVVM pattern with Jetpack Compose
- Use Repository pattern for data access
- Implement proper error handling
- Write unit tests for business logic

### **UI/UX Guidelines**
- Follow Material Design 3 principles
- Use MindBoost color palette consistently
- Ensure responsive design for different screen sizes
- Add proper accessibility labels

## 🧪 Testing

### **Unit Tests**
- Write tests for business logic
- Test validation functions
- Mock external dependencies
- Aim for >80% code coverage

### **UI Tests**
- Test critical user flows
- Verify navigation works correctly
- Test form validations
- Ensure proper error handling

## 📝 Pull Request Process

### **Before Submitting**
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Tests added/updated
- [ ] Documentation updated
- [ ] No breaking changes (or properly documented)

### **PR Description**
- Describe what changes were made
- Explain why changes were necessary
- Reference any related issues
- Include screenshots for UI changes

## 🐛 Bug Reports

When reporting bugs, please include:

### **Environment**
- Android version
- Device model
- App version
- Steps to reproduce

### **Expected vs Actual Behavior**
- What should happen
- What actually happens
- Screenshots or videos if helpful

## 💡 Feature Requests

When suggesting features:

### **Description**
- Clear description of the feature
- Use case and benefits
- Potential implementation approach
- Any alternatives considered

### **Priority**
- How important is this feature?
- How many users would benefit?
- Does it align with project goals?

## 🏗️ Project Structure

```
app/src/main/java/com/example/clase7/
├── ui/theme/           # Design system
├── models/             # Data models
├── screens/            # UI screens
├── utils/              # Utility functions
└── MainActivity.kt     # Main activity
```

## 🎨 Design System

### **Colors**
- Use MindBoost color palette
- Maintain consistency across screens
- Follow Material Design guidelines

### **Typography**
- Use MaterialTheme.typography
- Maintain proper hierarchy
- Ensure readability

### **Spacing**
- Use consistent spacing values
- Follow 8dp grid system
- Maintain proper padding/margins

## 📚 Resources

### **Documentation**
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

### **Tools**
- Android Studio
- Firebase Console
- GitHub Desktop (optional)

## 🚀 Getting Started

### **Setup Development Environment**
1. Clone the repository
2. Open in Android Studio
3. Configure Firebase project
4. Run the app

### **First Contribution**
- Look for issues labeled "good first issue"
- Start with small changes
- Ask questions in discussions
- Don't hesitate to reach out for help

## 📞 Contact

- **Maintainer**: Evan Tejada Duarte
- **Email**: evantejadaduarte@gmail.com
- **GitHub**: [@Evan21001](https://github.com/Evan21001)

## 📄 License

By contributing to MindBoost 2025, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to MindBoost 2025! 🎉
