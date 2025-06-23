export function generateGithubAuthUrl(purpose: 'login' | 'bind'): string {
    const clientId = 'Ov23liNIgiH2Q1DynsKP';
    const redirectUri = encodeURIComponent('http://slothask.online/callback/GitHubCallback');
    const scope = encodeURIComponent('read:user user:email');
     // 根据用途动态设置 state 参数
    return `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}&state=${purpose}`;
}
