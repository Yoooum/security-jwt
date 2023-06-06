<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import instance from '@/utils/request'

const router = useRouter()

const user = ref({})

const token = ref({
  accessToken: '',
  refreshToken: '',
})

function readToken() {
  token.value.accessToken = localStorage.getItem('accessToken')
  token.value.refreshToken = localStorage.getItem('refreshToken')
}
function fetchUser() {
  instance.get('/auth/user')
    .then((res) => {
      console.log(res)
      user.value = res
      window.$message.success('获取用户成功')
    })
    .catch((err) => {
      console.log(err)
      user.value = {}
      window.$message.error('获取用户信息失败，请重新登录')
      // logout()
    })
}

function logout() {
  localStorage.clear()
  router.push('/login')
}

readToken()
fetchUser()
</script>

<template>
  <n-card>
    <n-h3>用户信息</n-h3>
    <n-button-group>
      <n-button @click="fetchUser()">
        获取用户
      </n-button>
      <n-button @click="logout()">
        退出登录
      </n-button>
    </n-button-group>
    <n-code>
      <n-divider />
      <pre>{{ token }}</pre>
      <n-divider />
      <pre>{{ user }}</pre>
    </n-code>
  </n-card>
</template>

<style scoped>

</style>
