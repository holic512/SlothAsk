export function generateGithubAuthUrl(): string {
    const clientId = 'Ov23lil78EF0dPW8gXPU'
    const redirectUri = encodeURIComponent('http://localhost:3000/sign/GitHubCallback')
    const scope = encodeURIComponent('read:user user:email')
    const state = generateRandomState(16)

    return `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}&state=${state}`
}

function generateRandomState(length = 16): string {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
    let result = ''
    for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length))
    }
    return result
}
