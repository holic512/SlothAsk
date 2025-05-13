// passwordValidator.ts

/**
 * 校验结果接口
 */
export interface ValidationResult {
    valid: boolean;
    errors: string[];
}

/**
 * 默认的常见弱密码或违禁密码列表（可按需增删）
 */
const defaultBannedPasswords = [
    // 纯数字常见弱口令
    '1234', '12345', '123456', '1234567', '12345678', '123456789', '1234567890',
    '000000', '111111', '11111111', '121212',
    '696969', '123123', '123123123',

    // 常见字母组合
    'password', 'password1', 'passw0rd',
    'qwerty', 'qwerty123', 'qazwsx', 'zaq12wsx', 'zaq1zaq1', '123qwe',

    // 单词/短语
    'abc123', 'abc12345', 'letmein', 'welcome', 'dragon', 'sunshine',
    'master', 'superman', 'pokemon', 'football', 'baseball', 'monkey',
    'shadow', 'flower', 'hottie', 'lovely', 'freedom', 'whatever',

    // 个性化弱口令
    'admin', 'administrator', 'root', 'sysadmin', 'support', 'staff',
    'login', 'user', 'pass123', 'trustno1', 'iloveyou', '1234abcd',
    '123abc', '1q2w3e4r', 'master123', 'football1', 'baseball1'
];

// 特殊字符正则表达式
const SPECIAL_CHARS_PATTERN = /[!@#$%^&*()_\-=+[\]{};:'",.<>/?\\|]/;

/**
 * 密码校验选项
 */
export interface PasswordOptions {
    minLength?: number;           // 最小长度
    maxLength?: number;           // 最大长度
    requireUppercase?: boolean;   // 是否要求大写字母
    requireLowercase?: boolean;   // 是否要求小写字母
    requireDigit?: boolean;       // 是否要求数字
    requireSpecialChar?: boolean; // 是否要求特殊字符
    noWhitespace?: boolean;       // 是否禁止空白字符
    maxConsecutiveRepeat?: number;// 最大连续重复字符数，超过视为失败
    bannedList?: string[];        // 自定义的弱密码或违禁密码列表
    useBannedSubstringMatch?: boolean; // 是否启用违禁词子串匹配
}

/**
 * 默认密码校验规则
 */
const defaultOptions: Required<PasswordOptions> = {
    minLength: 8,
    maxLength: 32,
    requireUppercase: true,
    requireLowercase: true,
    requireDigit: true,
    requireSpecialChar: true,
    noWhitespace: true,
    maxConsecutiveRepeat: 2,
    bannedList: defaultBannedPasswords,
    useBannedSubstringMatch: true, // 默认启用子串匹配
};

/**
 * 校验密码
 * @param password 要校验的密码
 * @param options 可选校验规则配置
 * @returns {ValidationResult} 校验结果
 */
export function validatePassword(
    password: string | null | undefined,
    options: PasswordOptions = {}
): ValidationResult {
    const cfg = { ...defaultOptions, ...options };
    const errors: string[] = [];

    // 预处理输入
    if (password === null || password === undefined) {
        return {
            valid: false,
            errors: ['密码不能为空']
        };
    }
    
    // 去除前后空格
    password = password.trim();
    
    // 空密码检查
    if (password.length === 0) {
        return {
            valid: false,
            errors: ['密码不能为空']
        };
    }

    // 1. 长度校验
    if (password.length < cfg.minLength || password.length > cfg.maxLength) {
        errors.push(`长度必须在 ${cfg.minLength} 到 ${cfg.maxLength} 个字符之间`);
    }

    // 2. 大小写字母、数字、特殊字符校验
    if (cfg.requireUppercase && !/[A-Z]/.test(password)) {
        errors.push('必须包含至少一个大写字母 (A-Z)');
    }
    if (cfg.requireLowercase && !/[a-z]/.test(password)) {
        errors.push('必须包含至少一个小写字母 (a-z)');
    }
    if (cfg.requireDigit && !/[0-9]/.test(password)) {
        errors.push('必须包含至少一个数字 (0-9)');
    }
    if (cfg.requireSpecialChar && !SPECIAL_CHARS_PATTERN.test(password)) {
        errors.push('必须包含至少一个特殊字符（如 !@#$%^&*() 等）');
    }

    // 3. 禁止空白字符
    if (cfg.noWhitespace && /\s/.test(password)) {
        errors.push('不能包含空白字符');
    }

    // 4. 禁止连续重复字符过多
    if (cfg.maxConsecutiveRepeat > 0) {
        // 检查连续重复字符
        let repeatCount = 1;
        let lastChar = '';
        let hasRepeatError = false;
        
        for (let i = 0; i < password.length; i++) {
            const char = password[i];
            if (i > 0 && char === lastChar) {
                repeatCount++;
                if (!hasRepeatError && repeatCount > cfg.maxConsecutiveRepeat) {
                    errors.push(`不能有超过 ${cfg.maxConsecutiveRepeat} 个连续相同字符`);
                    hasRepeatError = true;
                }
            } else {
                repeatCount = 1;
            }
            lastChar = char;
        }
    }

    // 5. 弱密码/违禁密码检查
    const lower = password.toLowerCase();
    
    if (cfg.useBannedSubstringMatch) {
        // 子串匹配 - 检查密码是否包含任何违禁词
        for (const banned of cfg.bannedList) {
            if (banned && lower.includes(banned.toLowerCase())) {
                errors.push('密码包含常见弱密码或违禁词');
                break;
            }
        }
    } else {
        // 全匹配 - 检查密码是否与任何违禁词完全相同
        for (const banned of cfg.bannedList) {
            if (banned && lower === banned.toLowerCase()) {
                errors.push(`禁止使用弱密码 "${banned}"`);
                break;
            }
        }
    }

    return {
        valid: errors.length === 0,
        errors,
    };
}
