webpackJsonp([9],{l7gr:function(t,e){},lqpd:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a={props:{},data:function(){return{courseName:""}},computed:{},created:function(){var t=this;this.$http.get("/teaching/teacher/course/detail/"+this.$route.params.courseId).then(function(e){"0"==e.data.code?t.courseName=e.data.data.courseName:"1"==e.data.code?t.$router.push("/login"):t.$message({message:e.data.msg,type:"error"})}).catch(function(t){console.log(t)})},mounted:function(){},watch:{},methods:{},components:{}},i={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"page"},[a("div",{staticClass:"header"},[a("div",{staticClass:"containner"},[a("img",{attrs:{src:s("dkqV")}}),t._v(" "),a("h2",[t._v(t._s(t.courseName))])])]),t._v(" "),a("div",{staticClass:"containner"},[a("el-row",[a("el-col",{attrs:{span:4}},[a("el-menu",{staticClass:"el-menu-vertical-demo",attrs:{"default-active":"1",router:""}},[a("el-menu-item",{attrs:{index:"1",route:"/admin/courseList/experiment/"+this.$route.params.courseId}},[a("i",{staticClass:"el-icon-date"}),t._v(" "),a("span",{attrs:{slot:"title"},slot:"title"},[t._v("实验")])]),t._v(" "),a("el-menu-item",{attrs:{index:"2",route:"/admin/courseList/resource/"+this.$route.params.courseId}},[a("i",{staticClass:"el-icon-folder-opened"}),t._v(" "),a("span",{attrs:{slot:"title"},slot:"title"},[t._v("资源")])]),t._v(" "),a("el-menu-item",{attrs:{index:"3",route:"/admin/courseList/detail/"+this.$route.params.courseId}},[a("i",{staticClass:"el-icon-document-copy"}),t._v(" "),a("span",{attrs:{slot:"title"},slot:"title"},[t._v("课程详情")])]),t._v(" "),a("el-menu-item",{attrs:{index:"4",route:"/admin/courseList/manage/"+this.$route.params.courseId}},[a("i",{staticClass:"el-icon-medal-1"}),t._v(" "),a("span",{attrs:{slot:"title"},slot:"title"},[t._v("学生管理")])]),t._v(" "),a("el-menu-item",{attrs:{index:"5",route:"/admin/courseList/score/"+this.$route.params.courseId}},[a("i",{staticClass:"el-icon-edit-outline"}),t._v(" "),a("span",{attrs:{slot:"title"},slot:"title"},[t._v("实验打分")])])],1)],1),t._v(" "),a("el-col",{attrs:{span:20}},[a("router-view")],1)],1)],1)])},staticRenderFns:[]};var r=s("VU/8")(a,i,!1,function(t){s("l7gr")},"data-v-e7470598",null);e.default=r.exports}});
//# sourceMappingURL=9.23327c4af3943af0b96b.js.map