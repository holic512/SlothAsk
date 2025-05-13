export function generateGithubAuthUrl(purpose: 'login' | 'bind'): string {
    const clientId = 'Ov23lil78EF0dPW8gXPU';
    const redirectUri = encodeURIComponent('http://localhost:3000/callback/GitHubCallback');
    const scope = encodeURIComponent('read:user user:email');
     // 根据用途动态设置 state 参数
    return `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}&state=${purpose}`;
}
