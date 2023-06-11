<template>
  <!--  主页面-->
  <el-main>
    <!-- title-->
    <div class="font" style="margin-left: 150px; margin-top: 20px;">
      <div style="font-size: xxx-large" v-if="movie.obj.title!=null">
        {{movie.obj.title}}
      </div>
      <div style="font-size: xxx-large" v-else>
        暂无
      </div>
      <div style="color: #bbb; font-weight: bold" v-if="movie.obj.status">
        {{movie.obj.status}}
        {{movie.obj.release_date}}
        {{movie.obj.vote_average}}分
        {{rate}}
        <br>
        {{movie.obj.runtime}}分钟
        <div style="color: #bbb; font-weight: bold; display: inline-block" v-for="language in movie.obj.languages">
          {{language.name}}
        </div>
      </div>

<!--      测试-->
<!--      <div>{{movie}}</div>-->

    </div>
    <!-- 播放栏-->
    <div style="height: 400px; width: 800px; border: 1px solid white; margin: 20px auto">
      <img :src="`https://image.tmdb.org/t/p/w500${movie.obj.poster_path}`" class="image">
    </div>
    <!-- 信息-->
    <div style="margin-left: 150px; width: 1050px">
      <!-- tag-->
      <div style="margin-top: 10px; display: inline-block" v-for="genre in movie.obj.genres">
        <el-tag type="info" effect="plain" round style="background-color: black; color: white; font-size: 17px;text-align: center"
                color="white" size="large"> #{{genre.name}}<div>&#8197;</div>
        </el-tag>
      </div>
      <!-- 简介-->
      <div style="margin-top: 20px; font-size: 19px; color: #bbb; font-weight: bold">
        {{movie.obj.overview}}
      </div>
      <!-- 分割线-->
      <div class="divider"></div>
      <!-- 公司-->
      <div style="margin-top: 20px; display: flex; color: white;">
        <div style="font-size: 19px; font-weight: bold; margin-right: 10px">
          Company
        </div>
        <div style="margin-left: 20px; margin-top: 3px" v-for="company in movie.obj.companies">
          {{company.name}}&nbsp;
        </div>
      </div>
      <!-- 分割线-->
      <div class="divider"></div>
      <!-- 角色-->
      <div style="margin-top: 20px; display: flex; color: white">
        <div style="font-size: 19px; font-weight: bold; margin-right: 10px">
          Characters
        </div>
          <div style="margin-left: 20px; margin-top: 3px" v-for="cast in movie.obj.casts">
            {{cast.character}}
          </div>

<!--        <div style="margin-left: 20px; margin-top: 3px">-->
<!--          Ryan Gosling-->
<!--        </div>-->
<!--        <div style="margin-left: 20px; margin-top: 3px">-->
<!--          Ariana Greenblatt-->
<!--        </div>-->
      </div>
    </div>

    <!-- 评价-->
    <div>

    </div>

  </el-main>
</template>

<script>

import axios from "axios";
import {Briefcase} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";

export default {
  components: {Briefcase},
  data() {
    return {
      movie: null,
      rate: null,
    }
  },
  async created() {
    const movieId = this.$route.params.id;
    const response = await axios.get(`http://123.249.101.81:8080/movies/details/${movieId}`);
    if(response.data.code == 200) {
      this.movie = response.data;
    }
    try {
      const resp = await axios.get(`http://123.249.101.81:8080/movies/score/${movieId}`, {headers:{'token': localStorage.getItem('token')}})
      if (resp && resp.data && resp.data.code === 200) {
        this.rate = resp.data.message;
      } else {
        ElMessage(resp?.data?.message || '请求失败')
      }
    } catch (error) {
      console.log(error)
    }
    try {
      const resp = await axios.get(`http://123.249.101.81:8080/movies/score/${movieId}`, {headers:{'token': localStorage.getItem('token')}})
      if (resp && resp.data && resp.data.code === 200) {
        this.rate = resp.data.message;
      } else {
        ElMessage(resp?.data?.message || '请求失败')
      }
    } catch (error) {
      console.log(error)
    }
  }

}

</script>

<style scoped>
.font {
  color: white;
}

.divider {
  position: relative;
  height: 1px;
  margin-top: 15px;
  margin-bottom: 15px;
  background-color: rgba(50, 50, 50, 1);
  font-size: 16px;
  color: rgba(50, 50, 50, 1);
}
.image{
  max-width: 100%;
  max-height: 100%;
  min-width: 100%;
  min-height: 100%;
  /*min-height: 540px;*/
  /*min-width: 700px;*/
  /*border: white solid 1px;*/
}

</style>