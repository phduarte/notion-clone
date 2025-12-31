---
name: Pull Request
about: Create a pull request to contribute to this project
title: ''
labels: ''
assignees: ''

---

## 📋 Description

<!-- Describe what this PR does. What problem does it solve? -->

Closes #(issue)

## 🔄 Type of Change

<!-- Mark with an `x` all that apply -->

- [ ] 🐛 Bug fix (non-breaking change which fixes an issue)
- [ ] ✨ New feature (non-breaking change which adds functionality)
- [ ] 💥 Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] 📝 Documentation update
- [ ] 🎨 Style/UI update
- [ ] ♻️ Code refactor
- [ ] ⚡ Performance improvement
- [ ] ✅ Test update
- [ ] 🔧 Configuration change
- [ ] 🔒 Security fix

## 🧪 Testing

<!-- Describe the tests you ran and how to reproduce them -->

- [ ] Unit tests pass (`npm test` / `./gradlew test`)
- [ ] Integration tests pass
- [ ] Manual testing completed

### Test Configuration

**Browser/Environment**:
- OS: [e.g. Windows 11, macOS 14, Ubuntu 22.04]
- Browser: [e.g. Chrome 120, Firefox 121]
- Node version: [e.g. 20.10.0]

## 📸 Screenshots/Videos

<!-- If applicable, add screenshots or videos to help explain your changes -->

### Before
<!-- Screenshot/Video before changes -->

### After
<!-- Screenshot/Video after changes -->

## 📝 Checklist

<!-- Mark with an `x` all that apply -->

### Code Quality
- [ ] My code follows the style guidelines of this project
- [ ] I have performed a self-review of my own code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes

### Frontend (if applicable)
- [ ] ESLint passes (`npm run lint`)
- [ ] Prettier formatting applied
- [ ] TypeScript types are correct (`tsc --noEmit`)
- [ ] Build succeeds (`npm run build`)
- [ ] Components are responsive
- [ ] Accessibility (a11y) checked

### Backend (if applicable)
- [ ] Ktlint passes (`./gradlew ktlintCheck`)
- [ ] Detekt passes (`./gradlew detekt`)
- [ ] All tests pass (`./gradlew test`)
- [ ] Build succeeds (`./gradlew build`)
- [ ] Database migrations added (if needed)
- [ ] API documentation updated (Swagger)

### Security
- [ ] No sensitive data (passwords, keys, tokens) in code
- [ ] Input validation implemented
- [ ] SQL injection prevention checked
- [ ] XSS prevention checked
- [ ] CSRF protection maintained
- [ ] Authentication/Authorization checked

### Documentation
- [ ] README updated (if needed)
- [ ] API documentation updated (if needed)
- [ ] Inline code comments added
- [ ] CHANGELOG updated (if applicable)

## 🔗 Related Issues

<!-- Link related issues here -->

Relates to #
Depends on #
Blocks #

## 📋 Migration Notes

<!-- If this is a breaking change, explain how to migrate -->

### Breaking Changes
<!-- List any breaking changes -->

### Migration Steps
<!-- Provide step-by-step migration instructions -->

```bash
# Example migration steps
```

## 🚀 Deployment Notes

<!-- Any special instructions for deployment? -->

- [ ] Database migration required
- [ ] Environment variables changed
- [ ] Cache clear required
- [ ] Dependencies updated

### Environment Variables
<!-- List new or changed environment variables -->

```env
NEW_VAR=value
```

## 📊 Performance Impact

<!-- Describe any performance implications -->

- [ ] Performance tested
- [ ] No significant performance impact
- [ ] Performance improved
- [ ] Performance regression (explain why it's acceptable)

### Metrics
<!-- Add before/after metrics if applicable -->

## 🤝 Review Notes

<!-- Anything specific you want reviewers to focus on? -->

### Focus Areas
- 
- 

### Questions for Reviewers
- 
- 

## 📚 Additional Context

<!-- Add any other context about the PR here -->

---

**By submitting this pull request, I confirm that:**
- [ ] I have read and agree to the [Contributing Guidelines](../blob/master/CONTRIBUTING.md)
- [ ] I have read and agree to the [Code of Conduct](../blob/master/CODE_OF_CONDUCT.md)
- [ ] My contribution is made under the terms of the project license
