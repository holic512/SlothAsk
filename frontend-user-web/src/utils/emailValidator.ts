// emailValidator.ts

/**
 * 校验结果接口
 */
export interface ValidationResult {
    valid: boolean;
    errors: string[];
}

/**
 * 校验邮箱
 * 规则：
 * 1. 格式合法：符合通用邮箱格式规范（使用正则表达式）
 * 2. 不允许使用某些特殊邮箱域名（可选，例如临时邮箱）
 * 3. 不允许包含违禁词（不区分大小写，作用于邮箱前缀）
 *
 * @param email 要校验的邮箱地址
 * @param bannedWords 可选违禁词列表，默认使用 defaultBannedWords
 * @param bannedDomains 可选禁止域名列表，例如 ['tempmail.com', 'mailinator.com']
 * @returns {ValidationResult} 校验结果
 */
const defaultBannedWords = [
    'admin',
    'root',
    'test',
    'system',
    'support',
    'help',
];

const defaultBannedDomains = [
    'tempmail.com',
    'mailinator.com',
    '10minutemail.com',
];

export function validateEmail(
    email: string,
    bannedWords: string[] = defaultBannedWords,
    bannedDomains: string[] = defaultBannedDomains
): ValidationResult {
    const errors: string[] = [];

    // 1. 邮箱格式校验
    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    if (!emailRegex.test(email)) {
        errors.push('邮箱格式不合法');
    }

    const [localPart, domain] = email.toLowerCase().split('@');

    // 2. 校验域名是否在禁止列表中
    if (bannedDomains.includes(domain)) {
        errors.push(`不支持使用邮箱域名 "${domain}"`);
    }

    // 3. 校验邮箱前缀是否包含违禁词
    for (const word of bannedWords) {
        if (localPart.includes(word.toLowerCase())) {
            errors.push(`邮箱前缀不能包含违禁词 "${word}"`);
        }
    }

    return {
        valid: errors.length === 0,
        errors,
    };
}
