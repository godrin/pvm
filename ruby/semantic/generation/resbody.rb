class ResbodyCode
  def init
    @typeArray=tmpVar
    @blockName=tmpVar
    @condName=tmpVar
  end

  def ibefore
    @types.before+
    s("#{@typeArray}=#{@types.value}")+
    @types.after+
    s("#{@blockName}=begin()")+@body.code.indent+s("end")
  end

  def code

    ibefore+
    s("#{@condName}=#{@typeArray}.member?(exception)")+
    s("if #{@condName} then #{@blockName}")+
    iafter
  end

  def iafter
    s("clear #{@typeArray}")+
    s("clear #{@blockName}")+
    s("clear #{@condName}")
  end
end