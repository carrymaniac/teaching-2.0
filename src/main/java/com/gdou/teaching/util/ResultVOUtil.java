package com.gdou.teaching.util;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.vo.ResultVO;

public class ResultVOUtil {
    public static ResultVO success(Object o){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(o);
        return resultVO;
    }

    public static ResultVO fail(Integer code, String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        resultVO.setData(null);
        return resultVO;
    }
    public static ResultVO success(){
        return success(null);
    }

    public static ResultVO fail(ResultEnum resultEnum){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(resultEnum.getCode());
        resultVO.setMsg(resultEnum.getMsg());
        return resultVO;
    }
    public static ResultVO fail(TeachingException e){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(e.getCode());
        resultVO.setMsg(e.getMessage());
        return resultVO;
    }

}
