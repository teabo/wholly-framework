/**
 * @auther DangChengcheng 请保留作者
 * @mailTo dc2002007@163.com
 */

var Step_Tool_dc =function(ClassName,callFun){
    this.ClassName=ClassName,
    this.callFun=callFun,
    this.Steps = new Array(),
    this.stepAllHtml="";

}
Step_Tool_dc.prototype={
    /**
     * 绘制到目标位置
     */
     createStepArray:function(currStep,stepListJson){
        this.currStep=currStep;

            for (var i=0; i<stepListJson.length;i++){
            var  Step_Obj =new Step( this.currStep,stepListJson[i].StepNum,stepListJson[i].StepTitle,stepListJson[i].StepText,stepListJson[i].StepDate,stepListJson.length);

                Step_Obj.createStepHtml();
                this.Steps.push(Step_Obj);
            }

        },
    drawStep:function(currStep,stepListJson){
        this.clear();
        this.createStepArray(currStep,stepListJson);
        if(this.Steps.length>0){
        this.stepAllHtml+="<div class=\"steps\">";
        for (var i=0; i<this.Steps.length;i++){
            this.stepAllHtml+=this.Steps[i].htmlCode;
        }
        this.stepAllHtml+="</div>";
        $("."+this.ClassName).html(this.stepAllHtml);
            this.createEvent();
         } else{
            $("."+this.ClassName).html("没有任何步骤");
        }
    },createEvent:function(){
        var self=this;
        if (self.callFun){
        	$("."+this.ClassName+" div a").click(function(){
                var StepNum=$(this).attr("data-value");
                var StepTitle=$(this).attr("data-title");
                var StepText=$(this).attr("data-text");
                var StepDate=$(this).attr("data-date");
                result={StepNum:StepNum,StepTitle:StepTitle,StepText:StepText,StepDate:StepDate} ;
                eval(self.callFun+"(result)");
            });
        }
    }
    ,clear:function(){
        this.Steps=new Array();
        $("."+this.ClassName).html("");
        this.stepAllHtml="";
    }
}
var Step=function(currStep,StepNum,StepTitle,StepText,StepDate,totalCount){
        this.currStep=currStep,
        this.StepNum=StepNum ,
        this.StepTitle=StepTitle,
        this.StepText=StepText,
        this.StepDate=StepDate,
        this.totalCount=totalCount,
        this.htmlCode="";
}
Step.prototype={
    createStepHtml:function(){
        var stepHtml="\<div class=\"step_title\"\>"+this.StepTitle+"\</div\>";
        if(this.currStep>this.totalCount){
            this.currStep=this.totalCount;
        }else if(this.currStep<=0){this.currStep=1;}

        if(this.currStep>this.StepNum&&this.StepNum==1){
            classSype="firstFinshStep";
        } else if(this.currStep==this.StepNum&&this.StepNum==1){
            classSype="firstFinshStep_curr1";
        }
       else if(this.currStep==this.StepNum&&this.currStep!=this.totalCount){//当前步骤,下一个未进行,并且不是最后一个
            classSype="coressStep";
        }else  if(this.currStep==this.StepNum&&this.StepNum==this.totalCount){//当前步骤 并且是最后一步
            classSype="finshlast";
        }else if(this.currStep<this.StepNum&&this.StepNum==this.totalCount){//未进行步骤,并且是最后一个
            classSype="last";
        } else if(this.currStep<this.StepNum){//未进行的步骤
            classSype="loadStep";
        } else if(this.currStep>this.StepNum){//已进行的步骤
            classSype="finshStep";
        }
        stepHtml=stepHtml+"\<div class=\"num "+classSype+"\"\>"+this.StepNum+"\</div\>";
        stepHtml=stepHtml+"\<div class=\"step_text\"\>"+this.StepText+"\</div\>";
        stepHtml=stepHtml+"\<div class=\"step_date\"\>"+this.StepDate+"\</div\>";
        stepHtml="\<a class=\"step\" data-value=\""+this.StepNum+"\" data-title=\""+this.StepTitle+"\" data-text=\""+this.StepText+"\" data-date=\""+this.StepDate+"\"\>"+stepHtml+"\</a\>";
        this.htmlCode=stepHtml;
    }

}


