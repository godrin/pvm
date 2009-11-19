class LitCode
  def init
    @t=tmpVar
  end
  def before
    []
  end
  def after
    []
  end
  def before
    case @lit
    when Symbol
      s(@t+"=:"+@lit.to_s)
    else
      s(@t+"="+@lit.to_s)
    end
  end
  def value
    @t
  end
  def after
    s("clear #{@t}")
  end
  def code
    []
  end
end