webpackJsonp([8],{FXRG:function(e,t){},crLu:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n={props:{},data:function(){return{answerContent:"",answerFileList:[],loading:!0}},computed:{},created:function(){var e=this;this.$http.get("/teaching/student/experiment/answer/"+this.$route.params.experimentId).then(function(t){"0"==t.data.code?(e.answerContent=t.data.data.experimentAnswerContent||"",e.answerFileList=t.data.data.experimentAnswerFileList||[]):"1"==t.data.code?e.$router.push("/login"):e.$message({message:t.data.msg,type:"error"}),console.log("woyao loading..."),e.loading=!1,console.log("woyao loading out")}).catch(function(e){console.log(e)})},mounted:function(){},watch:{},methods:{},components:{spin:a("ly8Z").a}},s={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"page"},[n("spin",{attrs:{loading:e.loading}}),e._v(" "),e.loading?e._e():n("div",{staticClass:"container"},[n("div",[e._v(e._s(e.answerContent))]),e._v(" "),n("div",{staticClass:"footer"},[n("el-divider",[e._v("答案附件")]),e._v(" "),e._l(e.answerFileList,function(t,s){return n("div",{key:s},[n("a",{staticClass:"item",attrs:{href:t.filePath}},["doc"==t.fileType?n("img",{attrs:{src:a("OOXh"),alt:""}}):e._e(),e._v(" "),"ppt"==t.fileType?n("img",{attrs:{src:a("qb5D"),alt:""}}):e._e(),e._v(" "),"pdf"==t.fileType?n("img",{attrs:{src:a("o88e"),alt:""}}):e._e(),e._v(" "),n("span",{staticClass:"description"},[n("h3",[e._v(e._s(t.fileName))]),e._v(" "),n("div",[e._v(e._s(t.fileSize||0))]),e._v(" "),n("div",[e._v(e._s(t.createTime))])])])])})],2)])],1)},staticRenderFns:[]};var i=a("VU/8")(n,s,!1,function(e){a("FXRG")},"data-v-f9491d00",null);t.default=i.exports}});
//# sourceMappingURL=8.583447bc9a979ffd377b.js.map