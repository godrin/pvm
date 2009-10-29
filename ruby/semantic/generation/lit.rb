class LitCode
  def init
    #pp self
    #exit
  end
  def before
    []
  end
  def after
    []
  end
  def value
    case @lit
    when Symbol
      ":"+@lit.to_s
    else
      @lit.to_s
    end
  end
end