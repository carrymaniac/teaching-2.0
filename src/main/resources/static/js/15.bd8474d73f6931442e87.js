webpackJsonp([15],{Bswm:function(t,e){},HHjC:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i={props:{},data:function(){return{detail:"",loading:!0}},computed:{},created:function(){var t=this;this.$http.get("/teaching/student/course/detail/"+this.$route.params.detailId).then(function(e){"0"==e.data.code?t.detail=e.data.data:"1"==e.data.code?t.$router.push("/login"):t.$message({message:e.data.msg,type:"error"}),t.loading=!1}).catch(function(t){console.log(t)})},mounted:function(){},watch:{},methods:{},components:{spin:a("ly8Z").a}},s={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"page"},[i("div",{staticClass:"title"},[t._v("\n        全部\n    ")]),t._v(" "),t.loading||""!=t.detail?t._e():i("div",[i("el-alert",{attrs:{title:"暂无数据",type:"info",center:"","show-icon":""}})],1),t._v(" "),i("spin",{attrs:{loading:t.loading}}),t._v(" "),""!==t.detail?i("div",[i("div",{staticClass:"item"},[i("img",{attrs:{src:a("dkqV"),alt:""}}),t._v(" "),i("span",{staticClass:"description"},[i("div",[t._v("老师："+t._s(t.detail.teacherNickname))]),t._v(" "),i("div",[t._v("课程："+t._s(t.detail.courseName))]),t._v(" "),i("div",[t._v("代码："+t._s(t.detail.courseCode))]),t._v(" "),i("div",[t._v("学分："+t._s(t.detail.courseCredit))])])])]):t._e(),t._v(" "),i("div",{staticClass:"title"},[t._v("\n        基础介绍\n    ")]),t._v(" "),i("div",{staticClass:"introContent"},[t._v("\n        "+t._s(t.detail.courseIntroduction)+"\n    ")])],1)},staticRenderFns:[]};var n=a("VU/8")(i,s,!1,function(t){a("Bswm")},"data-v-66d0cb7e",null);e.default=n.exports}});
//# sourceMappingURL=15.bd8474d73f6931442e87.js.map