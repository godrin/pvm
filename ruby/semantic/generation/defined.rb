class DefinedCode
  def init
    @val=tmpVar
    @tmp=tmpVar
  end

  def before
    @what.before+
    s("#{@val}=#{@what.value}.nil?")+
    @what.after
  end

  def value
    @val
  end

  def after
    s("clear #{@val}")
  end
end