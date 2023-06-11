<template>
  <div style="padding-top: 15px; padding-left: 50px; color: #ffd04b">
    <h2>个人资料</h2>
  </div>
  <div style="padding-top: 5px">
    <el-form :model="state.user" label-width="80px" style="margin-left: 40px; margin-top: 20px">
      <el-row :gutter="50">
        <el-col :span="12">
          <el-form-item style="padding-bottom: 12px; margin-left: -50px">
            <span slot="label">
              <span style="color: white; margin-right: 10px">用户名</span>
            </span>
            <el-input style="width: 450px" v-model="state.user.userName" disabled></el-input>
          </el-form-item>
          <el-form-item style="padding-bottom: 12px; margin-left: -50px">
            <span slot="label">
              <span style="color: white; margin-right: 19px">昵 称</span>
            </span>
            <el-input style="width: 450px" v-model="state.user.nikeName"/>
          </el-form-item>
          <el-form-item style="padding-bottom: 12px; margin-left: -50px">
            <span slot="label">
              <span style="color: white; margin-right: 19px">邮 箱</span>
            </span>
            <el-input style="width: 450px" v-model="state.user.email" disabled></el-input>
          </el-form-item>
          <div>
            <el-form v-model="passwordVis" v-show="passwordVis" title="忘记密码" :close-on-click-modal="false" style="width: 535px;">
              <el-form :model="passwordForm" ref="rulePasswordFormRef" :rules="passwordRules" status-icon label-width="80px">
                <el-form-item style="padding-bottom: 12px; margin-left: 0" label="新密码" prop="password">
                  <el-input v-model="passwordForm.password" autocomplete="off"/>
                </el-form-item>
                <el-form-item style="padding-bottom: 12px; margin-left: 0" label="验证码" prop="verifyCode">
                  <div style="display: flex; width: 100%">
                    <el-input style="flex: 1" v-model="passwordForm.verifyCode" clearable></el-input>
                    <el-button style="width: 120px; margin-left: 5px" @click="sendEmail" :disabled="time > 0">点击发送<span
                        v-if="time">({{ time }})</span></el-button>
                  </div>
                </el-form-item>
              </el-form>
            </el-form>
          </div>
        </el-col>
        <el-col span="8">
            <img :src="`http://123.249.101.81:8080/users/avatars/${state.user.userName}.png`" style="height: 130px; width: 130px; border-style: solid; border-radius: 50%; border-color: white; border-width: 1px" alt="用户头像">
        </el-col>
        <el-col :span="6">
          <el-form-item label="">
            <el-upload
                class="avatar-uploader"
                :action="uploadUrl"
                :limit="1"
                :headers="headers"
                :before-upload="beforeUpload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :on-error="handleError"
                style="margin-left: -15px"
            >
<!--              <img v-if="false" :src="state.user.avatar" class="avatar"/>-->
              <el-icon  class="avatar-uploader-icon">
                <Plus/>
              </el-icon>
            </el-upload>
          </el-form-item>
          <div>
            <div style="font-size: 14px;padding-left: 40%">
              上传头像
            </div>
            <div style="font-size: 12px; color: #aaa; padding-left: 32%; margin-top: 10px">
              支持png格式图片
            </div>
          </div>
        </el-col>
      </el-row>
      <el-form-item label="">
        <el-button style="background-color:#ffd04b; color: white; font-weight: bold; border: none" @click="save">保 存
        </el-button>
        <el-button style="background-color:#ffd04b; color: white; font-weight: bold; border: none" @click="handleResetPassword" @dblclick="passwordVis=false">修改密码
        </el-button>
      </el-form-item>
    </el-form>
  </div>

</template>

<script setup>
import {useStore} from "vuex";
import {computed, nextTick} from "vue";

const store = useStore();
const user = computed(() => store.state.userData.obj);

import {Plus} from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";
import {reactive, ref} from "vue";
import request from "@/utils/request";
import axios from 'axios';

let state = reactive({
  user: user,
  headers: {
    Authorization: user.token
  }
})


let token = localStorage.getItem('token')
const uploadUrl = 'http://123.249.101.81:8080/users/changeAvartars';

const headers = {
  'Content-Type': 'multipart/form-data',
  'token': token,
};

const handleAvatarSuccess = (res) => {
  if (res.code === '200') {
    state.user.avatar = res.obj.avatar
  } else {
    ElMessage.error('上传失败')
  }
}

function handleError(error) {
  console.error(error);
}

function beforeUpload(file) {
  const formData = new FormData();
  formData.append('file', file.raw);
  axios({
    method: 'put',
    url: uploadUrl,
    headers: headers,
    data: formData,
  }).then(response => {
    if(response.code === '200'){
      state.user.avartar = response.obj.avatar;
      ElMessage.success(response.message)
    }else {
      ElMessage.error(response.message)
    }
  }).catch(error => {
    console.error(error);
  });
  return false;
}


const save = () => {
  // console.log(config)
  let config = {};
  let resteResult = false;
  console.log(state.user.userName)
  if (token) {
    config.headers = {'token': token};
  }
  let data = {
    'userName': state.user.userName,
    'nickName': state.user.nikeName
  }
  console.log(config)
  request.put('http://123.249.101.81:8080/users',data, config).then(res => {
    if(passwordVis == true){
      resetPassword(resteResult)
      if (res.code == '200' && resteResult == true) {
        ElMessage.success('保存成功')
        store.setUser(state.user)
      } else{
        ElMessage.error(res.message)
      }
    }else{
      if (res.code == '200') {
        ElMessage.success('保存成功')
        store.setUser(state.user)
      } else{
        ElMessage.error(res.message)
      }
    }
  })
    resetPassword()
}

const loadUser = () => {
  request.get('http://123.249.101.81:8080/users',{headers:{'token':token}}).then(res => {
    state.user.userName = res.obj.userName
    state.user.nikeName = res.obj.nickName
    state.user.avartar = res.obj.avartar
    state.user.role = res.obj.role
    state.user.email = res.obj.email
  })
}
loadUser()

const rulePasswordFormRef = ref()
const passwordVis = ref(false)
const interval = ref(-1)
const time = ref(0)
const passwordForm = reactive({})
passwordForm.userName = state.user.userName

const passwordRules = reactive({
  verifyCode: [
    {required: true, message: '请输入验证码', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入新的密码', trigger: 'blur'},
  ],
})

const times = () => {
  // 清空定时器
  if (interval.value >= 0) {
    clearInterval(interval.value)
  }
  time.value = 60
  interval.value = setInterval(() => {
    if (time.value > 0) {
      time.value--
    }
  }, 1000)
}

const sendEmail = () => {
  let data = {
    'userName': state.user.userName,
    'nickName': state.user.nikeName
  }
  axios.post("http://123.249.101.81:8080/users/verify",data,{
    headers:{
      Accept: '*/*',
      token: token
    }
  }).then(res => {
    if (res.code == '200') {
    times()  // 倒计时
    ElMessage.success(res.message)
    } else {
      ElMessage.error(res.message)
      console.log(res)
    }
  })
}

// 点击修改密码触发
const handleResetPassword = () => {
  passwordVis.value = true
  // 触发表单重置
  nextTick(() => {   // 下个时钟触发
    rulePasswordFormRef.value.resetFields()
  })
}

// 调用新接口重置密码
const resetPassword = (resetResult) => {
  rulePasswordFormRef.value.validate(valid => {
  // passwordForm.value.validate(valid => {
    if (valid) {
      request.put("http://123.249.101.81:8080/users/changepsw", passwordForm,{headers:{'token':token}}).then(res => {
        if (res.code == '200') {
          resetResult = true;
          ElMessage.success(res.message)
          // passwordVis.value = false
        } else {
          ElMessage.error(res.message)
        }
      })
    }
  })
}
</script>

<style scoped>

.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.whiteItem .el-form-item__label {
  color: white;
}
</style>