webpackJsonp([10],{AAZ5:function(e,t){},jLuS:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n={props:{},data:function(){return{tableDatas:[{name:"信管1班",list:[{userNumber:"2016-05-02",nickName:"王小虎",courseAchievement:"上海市普陀区金沙江路 1518 弄"},{userNumber:"2016-05-04",nickName:"王小虎",courseAchievement:"上海市普陀区金沙江路 1517 弄"}]},{name:"信管2班",list:[{userNumber:"2016-05-02",nickName:"王小虎",courseAchievement:"上海市普陀区金沙江路 1518 弄"},{userNumber:"2016-05-04",nickName:"王小虎",courseAchievement:"上海市普陀区金沙江路 1517 弄"}]}]}},computed:{},created:function(){var e=this;this.$http.get("/teaching/teacher/achievement/list/"+this.$route.params.courseId).then(function(t){"0"==t.data.code?e.tableDatas=t.data.data.tableDatas:"1"==t.data.code?e.$router.push("/login"):e.$message({message:t.data.msg,type:"error"})}).catch(function(e){console.log(e)})},mounted:function(){},watch:{},methods:{},components:{spin:a("ly8Z").a}},s={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"page"},[a("div",{staticClass:"title"},[e._v("\n        全部\n    ")]),e._v(" "),a("div",{staticClass:"filteBar"},[a("div",[a("el-button",{attrs:{type:"primary",size:"medium"},on:{click:function(t){return e.experimentAdd()}}},[a("i",{staticClass:"el-icon-user"}),e._v("班级管理\n            ")]),e._v(" "),a("el-button",{attrs:{type:"primary",size:"medium"},on:{click:function(t){return e.experimentAdd()}}},[a("i",{staticClass:"el-icon-folder-opened"}),e._v("导出成绩\n            ")])],1),e._v(" "),a("div",[a("el-select",{attrs:{size:"small",placeholder:"全部"},model:{value:e.value,callback:function(t){e.value=t},expression:"value"}},[a("el-option",[e._v("信管1班")]),e._v(" "),a("el-option",[e._v("信管2班")])],1)],1)]),e._v(" "),e._l(e.tableDatas,function(t,n){return a("div",{key:n,staticClass:"table"},[a("div",{staticClass:"title"},[e._v("\n            "+e._s(t.name)+"\n        ")]),e._v(" "),a("el-table",{attrs:{data:t.list,"row-class-name":e.tableRowClassName}},[a("el-table-column",{attrs:{prop:"userNumber",label:"学号"}}),e._v(" "),a("el-table-column",{attrs:{prop:"nickName",label:"学生名称"}}),e._v(" "),a("el-table-column",{attrs:{prop:"courseAchievement",label:"成绩"}}),e._v(" "),a("el-table-column",{attrs:{label:"操作",fixed:"right"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.routeToExperiment(t.row)}}},[e._v("\n                    详情\n                    ")])]}}],null,!0)})],1)],1)})],2)},staticRenderFns:[]};var l=a("VU/8")(n,s,!1,function(e){a("AAZ5")},"data-v-e7112e76",null);t.default=l.exports}});
//# sourceMappingURL=10.89b1a467ca761725186f.js.map