class ResbodyCode
  def init
    @typeArray=tmpVar
    @blockName=tmpVar
    @condName=tmpVar
  end

  def ibefore

    body_code=[]
    body_code=@body.code if @body
    body_value="nil"
    body_value=@body.value if @body

    @types.before+
    s("#{@typeArray}=#{@types.value}")+
    @types.after+
    bgn(@blockName,["*"],body_code,body_value)
    #s("#{@blockName}=begin()")+@body.code.indent+s("end")
  end

  def code

    ibefore+
    s("#{@condName}=#{@typeArray}.member?(exception)")+
    s("if #{@condName} then #{@blockName}")
  end

  def after
    s("clear #{@typeArray}")+
    s("clear #{@blockName}")+
    s("clear #{@condName}")
  end
end