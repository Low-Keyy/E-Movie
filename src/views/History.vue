<template>
    <!--    标题块-->
  <el-main>
    <div class="title-block">
      <div class="title" >
        历史记录
      </div>
<!--      <el-button style="margin-top: 37px;margin-left: 1280px;float: right;font-weight: bold;font-size: large; display: inline-block;color: white; background: red" type="submit" @click="deleteHistory">删除</el-button>-->
<!--      <span  style="margin-top: 37px;margin-left: 1280px;float: right;font-weight: bold;font-size: x-large; display: inline-block;color: white;">全选</span>-->
<!--      <input type="checkbox" style="margin-top: 40px;margin-left: 25px;margin-right: 10px; width: 30px; height: 30px" @click="change" :checked="isSelectAll" />-->
      <br>
    </div>
    <div style="margin-left: 30px; margin-right: 30px; background-color: #222222; display: inline-block" v-if="token">
      <br>
      <div>
        <!--        列表-->

<!--        测试-->
<!--        <div>{{movies}}</div>-->
        <el-form  v-for="(movie, index) in movies" :key="index" bodystyle="{padding 0 px}">
          <el-form-item>
            <div style="display: flex; flex-direction: row-reverse">
<!--              <input type="checkbox" style="width: 50px; height: 50px; margin-top: 100px; margin-right: 80px" @click="change" id="del_id" value="{{movie.movie.id}}"/>-->
<!--              <el-button style="margin-top: 80px;margin-right: 100px;float: right;font-weight: bold;font-size: large; display: inline-block;color: white; background: red" type="submit" @click="deleteHandler(movie.movie.id, movie.time)">删除</el-button>-->
              <el-card class="el-card" shadow="hover">
                <router-link :to="`/movies/details/${movie.movie.id}`">
                  <img :src="`https://image.tmdb.org/t/p/w500${movie.movie.poster_path}`" class="image">
                  <div style="margin-left: 140px; ">
                    <span class="font" style="font-weight: bold; font-size: 27px">{{movie.movie.original_title}}</span>
                    <br>
                    <span class="font" style="font-weight: bold; font-size: 17px; color: gray">发行日期 {{ movie.movie.release_date }}</span>
                    <br>
                    <span class="font" style="font-weight: bold; font-size: 17px; color: #bbbbbb">点击时间 {{ movie.time }}</span>
                    <br>
                    <span class="font" style="font-weight: bold; font-size: 17px; color: #bbbbbb">平均得分: {{ movie.movie.vote_average }}</span>
                    <br>
                    <div class="font" style="font-weight: bold; font-size: 18px; color: #bbbbbb; margin-top: 5px">简介： {{ movie.movie.overview }}</div>
                  </div>
                </router-link>
<!--                <el-button style="float:right; top: 0" @click="deleteHandler">删除记录</el-button>-->
              </el-card>
            </div>
          </el-form-item>
        </el-form>

      </div>
    </div>
    <div class="foot-block"></div>
  </el-main>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';
import {ElMessage} from "element-plus";

export default {
  components: {},

  setup(){
    const movies = ref([]);
    const token = localStorage.getItem('token');

    const getViewed = async (pageNum) => {
      // const token = localStorage.getItem('token');
      let url = `http://123.249.101.81:8080/users/movies/${pageNum}`;
      let config = {};

      if (token) {
        config.headers = { 'token': token };
      }

      try {
        const response = await axios.get(url, config);
        console.log()
        return response.data.obj.list;
      } catch (error) {
        console.error('Failed to get recommendations:', error);
      }
    }

    getViewed(1).then((recommendations) => {
      movies.value = recommendations;
      // console.log(movies.value)
      // console.log(movies)
    });
    const deleteHandler =(movieId,time)=> {
      let url = 'http://123.249.101.81:8080/users/movies';
      let config = {};
      let data = {
        movie_id: movieId,
        time: time,
      }

      if (token) {
        config.headers = {'token': token};
      }
      // config.body = {
      //   movie_id: movives//前端选中的电影id
      //   time:
      //
      // }
      axios.delete(url,data,config).then(response => {
        console.log(data)
        // return response.data.obj.list;
        if (response.code == '200') {
          ElMessage.success(response.message)
        } else {
          ElMessage.error(response.message)
        }
      })
    }

    return {token,movies,deleteHandler};
  }
}


// const isLoggedIn = () => {
//   const token = localStorage.getItem('token');
//   return !!token;
// }
// export default {
//   name: "History",
//   data() {
//     return {
//       historyList: [
//         {record:{
//             name: 'Friends',
//             castList: [
//               'Jennifer Aniston',
//               'Courteney Cox',
//               'Lisa Kudrow'
//             ],
//             imgSrc: 'src/assets/imgs/watchList/movie4.jpg',
//             tag: [
//               '1994-2004',
//               'TV Series',
//               'Comedy',
//               'Romance'
//             ],
//             time: new Date()
//           }},
//         {record:{
//             name: 'Unknown',
//             castList: [
//               'Unkown'
//             ],
//             imgSrc: 'src/assets/imgs/watchList/movie4.jpg',
//             tag: [
//               'TV Series'
//             ],
//             briefIntroduction:
//                 'Up next.',
//             time: new Date()
//           }},
//       ],
//     }
//   }
// }
</script>

<style>
.title-block {
  display: flex;
  height: 100px;
  margin-left: 30px;
  margin-right: 30px;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  background-color: #222222;
  border-bottom: 1px solid #999999;
}

.foot-block {
  min-height: 20px;
  margin-left: 30px;
  margin-right: 30px;
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
  background-color: #222222;
}

.title {
  margin-top: 20px;
  margin-left: 25px;
  margin-right: 10px;
  font-weight: bold;
  font-size: xxx-large;
  display: inline-block;
  color: white;
}

.el-card {
  margin-left: 20px;
  margin-right: 80px;
  margin-top: 30px;
  height: 200px;
  background-color: v-bind();
  border-color: v-bind();
  box-shadow: #999999;
}

.image {
  display: block;
  width: 110px;
  height: 165px;
  object-fit: fill;
  float: left;
}

</style>
