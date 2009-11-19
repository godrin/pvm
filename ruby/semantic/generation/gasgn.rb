class GasgnCode
  def init
    @tmp=tmpVar
  end
  def before
    []
  end
  def code
    if @value
      @value.before+
      s("#{@tmp}=#{@value.value}")+
      s("self$#{@name}=#{@tmp}")+
      @value.after
    else
      s("#{@tmp}=nil")+
      s("#{@name}=#{@tmp}")
    end
  end
  def value
    @tmp
  end
  def after
    s("clear #{@tmp}")
  end
end