webpackJsonp([14],{N3Tv:function(t,e){},xYIn:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i={props:{},data:function(){return{experimentList:[],loading:!0}},computed:{},created:function(){var t=this;this.$http.get("/teaching/student/course/resource/"+this.$route.params.detailId).then(function(e){"0"==e.data.code?(t.courseName=e.data.data.courseName,t.experimentList=e.data.data):"1"==e.data.code?t.$router.push("/login"):t.$message({message:e.data.msg,type:"error"}),t.loading=!1}).catch(function(t){console.log(t)})},mounted:function(){},watch:{},methods:{},components:{spin:a("ly8Z").a}},s={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"page"},[i("div",{staticClass:"title"},[t._v("\n        全部\n    ")]),t._v(" "),!t.loading&&0==t.experimentList.length||!t.experimentList?i("div",[i("el-alert",{attrs:{title:"暂无数据",type:"info",center:"","show-icon":""}})],1):t._e(),t._v(" "),i("spin",{attrs:{loading:t.loading}}),t._v(" "),t._l(t.experimentList,function(e,s){return i("div",{key:s},[i("a",{staticClass:"item",attrs:{href:e.filePath}},["doc"==e.fileType?i("img",{attrs:{src:a("OOXh"),alt:""}}):t._e(),t._v(" "),"ppt"==e.fileType?i("img",{attrs:{src:a("qb5D"),alt:""}}):t._e(),t._v(" "),"pdf"==e.fileType?i("img",{attrs:{src:a("o88e"),alt:""}}):t._e(),t._v(" "),i("span",{staticClass:"description"},[i("h3",[t._v(t._s(e.fileName))]),t._v(" "),i("div",[t._v(t._s(e.fileSize||0))]),t._v(" "),i("div",[t._v(t._s(e.createTime))])])])])})],2)},staticRenderFns:[]};var n=a("VU/8")(i,s,!1,function(t){a("N3Tv")},"data-v-4395bab5",null);e.default=n.exports}});
//# sourceMappingURL=14.dd38733032daa41d98ae.js.map