// usernameValidator.ts

/**
 * 校验结果接口
 */
export interface ValidationResult {
    valid: boolean;
    errors: string[];
}

/**
 * 默认的违禁词列表（可按需增删）
 */
const defaultBannedWords = [
    'admin',         // 管理员相关
    'administrator',
    'root',          // 系统最高权限
    'sysadmin',
    'superuser',
    'manager',       // 经理、管理者
    'moderator',     // 版主
    'operator',      // 运营
    'support',       // 客服
    'helpdesk',
    'staff',         // 员工
    'owner',         // 所有人
    'system',        // 系统
    'guest',         // 游客
    'test',          // 测试账号
    'info',          // 信息发布
    'host',          // 主机
    'developer',     // 开发者
    'rootuser',
    'supervisor',
];

/**
 * 校验用户名
 * 规则：
 * 1. 长度限制：4~20 个字符
 * 2. 允许字符：英文字母（大小写不限）、数字、下划线（_）、点（.）、连字符（-）
 * 3. 点（.）或连字符（-）：
 *    - 不能出现在首尾
 *    - 不能连续出现（如 ".." 或 "--"）
 * 4. 违禁词过滤：用户名中不能包含违禁词列表内的任何词（不区分大小写）
 *
 * @param username 要校验的用户名
 * @param bannedWords 可选自定义违禁词列表，默认为 defaultBannedWords
 * @returns {ValidationResult} 校验结果
 */
export function validateUsername(
    username: string,
    bannedWords: string[] = defaultBannedWords
): ValidationResult {
    const errors: string[] = [];

    // 1. 长度校验：4~20 个字符
    if (username.length < 4 || username.length > 20) {
        errors.push('长度必须在 4 到 20 个字符之间');
    }

    // 2. 字符合法性：仅允许字母、数字、下划线、点、连字符
    if (!/^[A-Za-z0-9_.-]+$/.test(username)) {
        errors.push('仅允许英文字母、数字、下划线（_）、点（.）或连字符（-）');
    }

    // 3. 点/连字符 不能出现在首尾
    if (/^[.-]/.test(username) || /[.-]$/.test(username)) {
        errors.push('点（.）或连字符（-）不能作为首尾字符');
    }

    // 4. 禁止连续出现同类特殊字符（如 .. 或 --）
    if (/\.\./.test(username) || /--/.test(username)) {
        errors.push('点（.）或连字符（-）不能连续出现（如 ".."、"--"）');
    }

    // 5. 违禁词检查（不区分大小写）
    const lower = username.toLowerCase();
    for (const word of bannedWords) {
        if (word && lower.includes(word.toLowerCase())) {
            errors.push(`用户名不能包含违禁词 "${word}"`);
        }
    }

    return {
        valid: errors.length === 0,
        errors,
    };
}
