<script setup>
import instance from '@/utils/request'
import {useMessage} from 'naive-ui'
import {ref} from 'vue'
import {useRouter} from "vue-router";

const router = useRouter()

const message = useMessage()
const form = ref({
  username: 'admin',
  password: '123456',
})

function login() {
  instance.post('/auth/token', null, {
    params: {
      username: form.value.username,
      password: form.value.password
    }
  }).then((res) => {
    message.success('登录成功')
    const { accessToken, refreshToken} = res
    localStorage.setItem('accessToken', accessToken)
    localStorage.setItem('refreshToken', refreshToken)
    router.push('/')
  })
}
</script>

<template>
  <n-space justify="center" align="center" class="h-100vh flex">
    <n-card class="w-300px">
      <n-tabs>
        <n-tab-pane name="login" tab="登录">
          <n-form>
            <n-input
                v-model:value="form.username" class="mb-6 mt-2"
                type="text" placeholder="账号"
            />
            <n-input
                v-model:value="form.password" class="mb-6"
                type="password" placeholder="密码" show-password-on="click"
            />
          </n-form>
          <n-button type="primary" block strong @click="login()">
            登录
          </n-button>
          <n-divider/>
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </n-space>
</template>

<style scoped>

</style>
