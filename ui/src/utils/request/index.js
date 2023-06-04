import axios from 'axios'

const instance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000,
})

axios.defaults.baseURL = instance.defaults.baseURL

// 请求拦截器
instance.interceptors.request.use(
    config => {
        // 在发送请求之前：设置请求头、添加请求参数、处理跨域问题等
        const accessToken = localStorage.getItem('accessToken')
        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`
        }
        return config
    },
    error => {
        // 对请求错误做些什么
        return Promise.reject(error)
    }
)

// 响应拦截器
instance.interceptors.response.use(
    response => {
        // 对响应数据做点什么：检查响应状态码、解析响应数据等
        const {
            code,
            message,
            data
        } = response.data
        // 请求成功
        if (code === 0) {
            console.log(response)
            return Promise.resolve(data)
        }

        // 请求失败
        if (code !== 0 && message) {
            window.$message.error('[拦截器][业务异常]' + message)
        }
        console.log(response)
        return Promise.reject(response.data)
    },
    async error => {
        console.log(error)
        // 对响应错误做些什么
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken && error.response.status === 401) {
            console.log('开始刷新令牌')
            console.log('当前原始请求', error)
            try {
                const response = await axios.get(`/auth/token?refreshToken=${refreshToken}`);
                console.log('新访问令牌 ' + response.data?.data.accessToken)
                console.log('新刷新令牌 ' + response.data?.data.refreshToken)
                localStorage.setItem('accessToken', response.data?.data.accessToken);
                localStorage.setItem('refreshToken', response.data?.data.refreshToken);
                error.response.config.headers.Authorization = `Bearer ${response.data?.data.accessToken}`;
                console.log('重试原始请求', error)
                return axios(error.response.config);
            } catch (error) {
                console.log('令牌刷新失败', error)
                window.$message.error('[令牌刷新失败]' + error.response?.data.message);
                console.log('开始清除令牌')
                localStorage.clear();
                return Promise.reject(error);
            }
        } else {
            console.log('跳过刷新令牌')
        }
        window.$message.error('[拦截器][业务异常]' + error.response?.data.message)
        return Promise.reject(error)
    },
)

export default instance


