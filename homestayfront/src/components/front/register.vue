<template>
    <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="120px" class="demo-ruleForm"
        :size="formSize" status-icon>
        <el-form-item label="用户名" prop="username">
            <el-input v-model="ruleForm.username" style="width: 80%" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
            <el-input v-model="ruleForm.password" type="password" placeholder="请输入密码" show-password
                style="width: 80%" />
        </el-form-item>
        <!-- <el-form-item label="确认密码" prop="checkPass">
            <el-input v-model="ruleForm.checkPass" type="password" autocomplete="off" />
        </el-form-item> -->
        <el-form-item label="手机号" prop="phone">
            <el-input v-model="ruleForm.phone" style="width: 80%" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="ruleForm.gender">
                <el-radio label="男" />
                <el-radio label="女" />
                <el-radio label="保密" />
            </el-radio-group>
        </el-form-item>

        <el-form-item label="验证码" prop="validCode">
            <div style="display: flex">
                <el-input v-model="ruleForm.validCode" style="width: 40%;height: 40px;margin-right:10px;"
                    placeholder="验证码"></el-input>
                <ValidCode @imgCode="createImgCode" />
            </div>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="submitForm(ruleFormRef)">创建</el-button>
            <el-button @click="resetForm(ruleFormRef)">重置</el-button>
        </el-form-item>
    </el-form>
</template>
  
<script lang="ts" setup>
import { reactive, ref, defineEmits } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import request from '@/utils/request';
import { ElMessage } from 'element-plus'
import ValidCode from '@/components/ValidCode.vue';
const formSize = ref('default')
const ruleFormRef = ref<FormInstance>()

const ruleForm = reactive({
    username: '',
    password: '',
    // checkPass: '',
    phone: '',
    gender: '',
    validCode: ''
})
// const validatePass2 = (rule: any, value: any, callback: any) => {
//     // 密码二次验证
//     if (value === '') {
//         callback(new Error('请再次输入密码'))
//     } else if (value !== ruleForm.password) {
//         callback(new Error("两次密码输入不一致！"))
//     } else {
//         callback()
//     }
// }
const rules = reactive<FormRules>({
    username: [
        { required: true, message: '请输入登录用户名', trigger: 'blur' },
        { min: 4, max: 16, message: '长度在4-16个字符', trigger: 'blur' },
        { pattern: /^[a-zA-Z0-9_-]{4,16}$/, message: '格式不对,仅支持字母、数字、下划线、-', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 16, message: '长度在6-16个字符15', trigger: 'blur' },
    ],
    // checkPass: [
    //     { required: true, message: '请再次输入密码', trigger: 'blur' },
    //     { validator: validatePass2, trigger: 'blur' }
    // ],
    phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^(?:(?:\+|00)86)?1\d{10}$/, message: '号码格式不对', trigger: 'blur' }
    ],
    gender: [
        { required: true, message: '请选择性别' }
    ],
    validCode: [
        { required: true, message: '请输入验证码', trigger: 'blur' }
    ],

})
//验证码生成并获取子级组件中的验证码
const imgCode = ref('')
const createImgCode = (data: any) => {  //获取子级组件返回的验证码，进行比对
    // console.log(data)
    imgCode.value = data
}
// 返回父组件结果，控制弹窗显示布尔值
const emit = defineEmits<{
    (e: 'res', res: boolean): void
}>()
const submitForm = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
        if (valid) {
            // console.log('submit!')
            // console.log(ruleForm);
            // 规则验证通过后进行注册处理
            if (ruleForm.validCode.toLowerCase() == imgCode.value.toLowerCase()) {  //全部转小写在进行比对
                request.post('/h/user', ruleForm).then((res: any) => {
                    if (res.code === '0' || res.code === '200') {
                        ElMessage.success('注册成功，请登录')
                        emit('res', false)

                    } else {
                        ElMessage.error(res.msg)
                    }
                }).catch(err => {
                    ElMessage.error('注册失败')
                })
            }else {
                ElMessage.error('验证码错误')
            }


        } else {
            console.log('error submit!', fields)
        }
    })
}

const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
}

const options = Array.from({ length: 10000 }).map((_, idx) => ({
    value: `${idx + 1}`,
    label: `${idx + 1}`,
}))
</script>
  